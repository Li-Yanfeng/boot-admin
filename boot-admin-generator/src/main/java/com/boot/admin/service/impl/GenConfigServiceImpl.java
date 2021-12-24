package com.boot.admin.service.impl;

import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mapper.GenConfigMapper;
import com.boot.admin.model.GenConfig;
import com.boot.admin.service.GenConfigService;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 代码生成配置 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    @Override
    public void saveGenConfig(GenConfig resource) {
        if (resource.getConfigId() != null) {
            baseMapper.updateById(resource);
        } else {
            baseMapper.insert(resource);
        }
    }

    @Override
    public void removeGenConfigByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public GenConfig getGenConfigById(Long id) {
        GenConfig genConfig = baseMapper.selectById(id);
        ValidationUtils.notNull(genConfig, "GenConfig", "configId", id);
        return ConvertUtils.convert(genConfig, GenConfig.class);
    }

    @Override
    public GenConfig getGenConfigByTableName(String tableName) {
        GenConfig genConfig = lambdaQuery()
            .eq(GenConfig::getTableName, tableName)
            .one();
        if (genConfig == null) {
            return new GenConfig(tableName);
        }
        return genConfig;
    }
}
