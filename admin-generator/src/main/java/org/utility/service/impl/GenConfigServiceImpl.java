package org.utility.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.mapper.GenConfigMapper;
import org.utility.model.GenConfig;
import org.utility.service.GenConfigService;

/**
 * 代码生成配置 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class GenConfigServiceImpl implements GenConfigService {

    private final GenConfigMapper genConfigMapper;

    public GenConfigServiceImpl(GenConfigMapper genConfigMapper) {
        this.genConfigMapper = genConfigMapper;
    }

    @Override
    public GenConfig getByTableName(String tableName) {
        LambdaQueryWrapper<GenConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GenConfig::getTableName, tableName);
        GenConfig genConfig = genConfigMapper.selectOne(wrapper);
        if (genConfig == null) {
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Override
    public GenConfig update(String tableName, GenConfig genConfig) {
        if (genConfig.getId() == null) {
            genConfigMapper.insert(genConfig);
        } else {
            genConfigMapper.updateById(genConfig);
        }
        return genConfig;
    }
}
