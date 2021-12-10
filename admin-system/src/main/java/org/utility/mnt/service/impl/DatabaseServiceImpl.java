package org.utility.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mnt.mapper.DatabaseMapper;
import org.utility.mnt.model.Database;
import org.utility.mnt.service.DatabaseService;
import org.utility.mnt.service.dto.DatabaseDTO;
import org.utility.mnt.service.dto.DatabaseQuery;
import org.utility.mnt.util.SqlUtils;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;
import org.utility.util.QueryHelp;
import org.utility.util.ValidationUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        ValidationUtils.notNull(baseMapper.selectById(dbId), "Database", "dbId", dbId);
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
        ValidationUtils.notNull(database, "Database", "dbId", id);
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
    public void exportDatabase(List<DatabaseDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(database -> {
            Map<String, Object> map = MapUtil.newHashMap(4, true);
            map.put("数据库名称", database.getName());
            map.put("数据库连接地址", database.getJdbcUrl());
            map.put("用户名", database.getUserName());
            map.put("创建日期", database.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
