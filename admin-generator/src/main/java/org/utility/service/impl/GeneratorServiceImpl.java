package org.utility.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.api.Result;
import org.utility.exception.BadRequestException;
import org.utility.mapper.ColumnInfoMapper;
import org.utility.model.ColumnInfo;
import org.utility.model.GenConfig;
import org.utility.model.vo.TableInfo;
import org.utility.service.GeneratorService;
import org.utility.util.FileUtils;
import org.utility.util.GenUtils;
import org.utility.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private final ColumnInfoMapper columnInfoMapper;

    public GeneratorServiceImpl(ColumnInfoMapper columnInfoMapper) {
        this.columnInfoMapper = columnInfoMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<ColumnInfo> columnInfos) {
        for (ColumnInfo info : columnInfos) {
            if (info.getId() == null) {
                columnInfoMapper.insert(info);
            } else {
                columnInfoMapper.updateById(info);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnInfo columnInfo : columnInfoList) {
            // 根据字段名称查找
            List<ColumnInfo> columns =
                    columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnInfo column = columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                columnInfoMapper.updateById(column);
            } else {
                // 如果找不到，则保存新字段信息
                columnInfoMapper.insert(columnInfo);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnInfo columnInfo : columnInfos) {
            // 根据字段名称查找
            List<ColumnInfo> columns =
                    columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                columnInfoMapper.deleteById(columnInfo.getId());
            }
        }
    }

    @Override
    public List<TableInfo> listTables() {
        return columnInfoMapper.selectListTables();
    }

    @Override
    public IPage<TableInfo> pageTables(IPage page, String tableName) {
        return columnInfoMapper.selectPageTables(page, "%" + tableName + "%");
    }

    @Override
    public List<ColumnInfo> listColumnInfos(String tableName) {
        return columnInfoMapper.selectByTableName(tableName);
    }

    @Override
    public List<ColumnInfo> listColumns(String tableName) {
        LambdaQueryWrapper<ColumnInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ColumnInfo::getTableName, tableName).orderByAsc(ColumnInfo::getId);
        List<ColumnInfo> columnInfos = columnInfoMapper.selectList(wrapper);
        // 先从生成的列信息记录中获取
        if (CollectionUtil.isNotEmpty(columnInfos)) {
            return columnInfos;
        } else {
            columnInfos = listColumnInfos(tableName);
            for (ColumnInfo info : columnInfos) {
                columnInfoMapper.insert(info);
            }
            return columnInfos;
        }
    }

    @Override
    public Result preview(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        List<Map<String, Object>> genList = GenUtils.preview(columns, genConfig);
        return Result.success(genList);
    }

    @Override
    public void generator(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        try {
            GenUtils.generatorCode(columns, genConfig);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new BadRequestException("生成失败，请手动处理已生成的文件");
        }
    }

    @Override
    public void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response) {
        if (genConfig.getId() == null) {
            throw new BadRequestException("请先配置生成器");
        }
        try {
            File file = new File(GenUtils.download(columns, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtils.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new BadRequestException("打包失败");
        }
    }
}

