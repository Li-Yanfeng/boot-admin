package org.utility.service.impl;

import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mapper.GenConfigMapper;
import org.utility.model.GenConfig;
import org.utility.service.GenConfigService;
import org.utility.util.ConvertUtils;
import org.utility.util.ValidationUtils;

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
