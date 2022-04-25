package com.boot.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mapper.ColumnConfigMapper;
import com.boot.admin.model.ColumnConfig;
import com.boot.admin.model.GenConfig;
import com.boot.admin.service.ColumnConfigService;
import com.boot.admin.util.Assert;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.GenUtils;
import com.boot.admin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成字段信息存储 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class ColumnConfigServiceImpl extends ServiceImpl<ColumnConfigMapper, ColumnConfig> implements ColumnConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ColumnConfigServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveColumnConfig(List<ColumnConfig> resources) {
        resources.forEach(resource -> {
            if (resource.getColumnId() != null) {
                baseMapper.updateById(resource);
            } else {
                baseMapper.insert(resource);
            }
        });
    }

    @Override
    public void removeColumnConfigByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncColumnConfig(List<ColumnConfig> resources, List<ColumnConfig> resourceList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnConfig columnConfig : resourceList) {
            // 根据字段名称查找
            List<ColumnConfig> columns =
                resources.stream().filter(c -> c.getColumnName().equals(columnConfig.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollUtil.isNotEmpty(columns)) {
                ColumnConfig column = columns.get(0);
                column.setColumnType(columnConfig.getColumnType());
                column.setExtra(columnConfig.getExtra());
                column.setKeyType(columnConfig.getKeyType());
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnConfig.getRemark());
                }
                baseMapper.updateById(column);
            } else {
                // 如果找不到，则保存新字段信息
                baseMapper.insert(columnConfig);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnConfig columnConfig : resources) {
            // 根据字段名称查找
            List<ColumnConfig> columns =
                resourceList.stream().filter(c -> c.getColumnName().equals(columnConfig.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                baseMapper.deleteById(columnConfig.getColumnId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ColumnConfig> listColumnConfigs(String tableName, boolean queryFromInformationSchema) {
        List<ColumnConfig> columnConfigs;
        if (queryFromInformationSchema) {
            columnConfigs = baseMapper.selectListFromInformationSchema(tableName);
        } else {
            columnConfigs = lambdaQuery()
                .eq(ColumnConfig::getTableName, tableName)
                .orderByAsc(ColumnConfig::getColumnId)
                .list();

            // 先从生成的列信息记录中获取
            if (CollUtil.isEmpty(columnConfigs)) {
                columnConfigs = baseMapper.selectListFromInformationSchema(tableName);
                this.saveColumnConfig(columnConfigs);
            }
        }
        return columnConfigs;
    }

    @Override
    public Page<ColumnConfig> listColumnConfigs(String tableName, Page<ColumnConfig> page) {
        Page<ColumnConfig> columnConfigs = lambdaQuery()
            .eq(ColumnConfig::getTableName, tableName)
            .orderByAsc(ColumnConfig::getColumnId)
            .page(page);

        // 先从生成的列信息记录中获取
        if (CollUtil.isEmpty(columnConfigs.getRecords())) {
            this.saveColumnConfig(baseMapper.selectListFromInformationSchema(tableName));
            columnConfigs = baseMapper.selectPageFromInformationSchema(page, tableName);
        }
        return columnConfigs;
    }

    @Override
    public ColumnConfig getColumnConfigById(Long id) {
        ColumnConfig columnConfig = baseMapper.selectById(id);
        Assert.notNull(columnConfig);
        return columnConfig;
    }

    @Override
    public List<Map<String, Object>> preview(GenConfig genConfig, List<ColumnConfig> columnConfigs) {
        if (genConfig.getConfigId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        return GenUtils.preview(columnConfigs, genConfig);
    }

    @Override
    public void generator(GenConfig genConfig, List<ColumnConfig> columnConfigs) {
        if (genConfig.getConfigId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        try {
            GenUtils.generatorCode(columnConfigs, genConfig);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new BadRequestException("生成失败，请手动处理已生成的文件");
        }
    }

    @Override
    public void download(GenConfig genConfig, List<ColumnConfig> columnConfigs, HttpServletRequest request,
                         HttpServletResponse response) {
        if (genConfig.getConfigId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        try {
            File file = new File(GenUtils.download(columnConfigs, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtils.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new BadRequestException("打包失败");
        }
    }
}
