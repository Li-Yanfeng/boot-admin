package org.utility.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.mapper.GenConfigMapper;
import org.utility.model.GenConfig;
import org.utility.service.GenConfigService;

import java.io.File;

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
        // 如果 api 路径为空，则自动生成路径
        if (StrUtil.isBlank(genConfig.getApiPath())) {
            String separator = File.separator;
            String[] paths;
            String symbol = "\\";
            if (symbol.equals(separator)) {
                paths = genConfig.getPath().split("\\\\");
            } else {
                paths = genConfig.getPath().split(File.separator);
            }
            StringBuilder api = new StringBuilder();
            for (String path : paths) {
                api.append(path);
                api.append(separator);
                if ("src".equals(path)) {
                    api.append("api");
                    break;
                }
            }
            genConfig.setApiPath(api.toString());
        }
        if (genConfig.getId() == null) {
            genConfigMapper.insert(genConfig);
        } else {
            genConfigMapper.updateById(genConfig);
        }
        return genConfig;
    }
}
