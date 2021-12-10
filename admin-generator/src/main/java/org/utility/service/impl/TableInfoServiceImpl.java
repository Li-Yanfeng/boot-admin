package org.utility.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mapper.TableInfoMapper;
import org.utility.model.TableInfo;
import org.utility.service.TableInfoService;

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
