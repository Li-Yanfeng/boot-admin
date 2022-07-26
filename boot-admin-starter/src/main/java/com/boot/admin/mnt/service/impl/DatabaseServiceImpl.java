package com.boot.admin.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mnt.mapper.DatabaseMapper;
import com.boot.admin.mnt.model.Database;
import com.boot.admin.mnt.service.DatabaseService;
import com.boot.admin.mnt.service.dto.DatabaseDTO;
import com.boot.admin.mnt.service.dto.DatabaseQuery;
import com.boot.admin.mnt.util.SqlUtils;
import com.boot.admin.util.Assert;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.QueryHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"database"})
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, Database> implements DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Override
    public void saveDatabase(Database resource) {
        baseMapper.insert(resource);
    }

    @Override
    public void removeDatabaseByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateDatabaseById(Database resource) {
        Long dbId = resource.getDbId();
        Assert.notNull(baseMapper.selectById(dbId));
        baseMapper.updateById(resource);
    }

    @Override
    public List<DatabaseDTO> listDatabases(DatabaseQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DatabaseDTO.class);
    }

    @Override
    public Page<DatabaseDTO> listDatabases(DatabaseQuery query, Page<Database> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), DatabaseDTO.class);
    }

    @Override
    public DatabaseDTO getDatabaseById(Long id) {
        Database database = baseMapper.selectById(id);
        Assert.notNull(database);
        return ConvertUtils.convert(database, DatabaseDTO.class);
    }

    @Override
    public boolean testConnection(Database resources) {
        try {
            return SqlUtils.testConnection(resources.getJdbcUrl(), resources.getUserName(), resources.getPwd());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void exportDatabase(List<DatabaseDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(database -> {
            Map<String, Object> map = MapUtil.newHashMap(4, true);
            map.put("数据库名称", database.getName());
            map.put("数据库连接地址", database.getJdbcUrl());
            map.put("用户名", database.getUserName());
            map.put("创建日期", database.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel("数据库", list, response);
    }
}
