package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;
import org.utility.base.impl.ServiceImpl;
import org.utility.modules.mnt.mapper.DatabaseMapper;
import org.utility.modules.mnt.model.Database;
import org.utility.modules.mnt.service.DatabaseService;
import org.utility.modules.mnt.service.dto.DatabaseDTO;
import org.utility.modules.mnt.service.dto.DatabaseQuery;
import org.utility.modules.mnt.util.SqlUtils;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, DatabaseDTO, DatabaseQuery, Database> implements DatabaseService {

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
    public void download(HttpServletResponse response, List<DatabaseDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (DatabaseDTO database : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(4, true);
            map.put("数据库名称", database.getName());
            map.put("数据库连接地址", database.getJdbcUrl());
            map.put("用户名", database.getUserName());
            map.put("创建日期", database.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
