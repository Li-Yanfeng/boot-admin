package com.boot.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mapper.TableInfoMapper;
import com.boot.admin.model.TableInfo;
import com.boot.admin.service.TableInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表的数据信息 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

    @Override
    public List<TableInfo> listTableInfos() {
        return baseMapper.selectList();
    }

    @Override
    public Page<TableInfo> listTableInfos(String tableName, Page<TableInfo> page) {
        return baseMapper.selectPage(page, tableName);
    }
}
