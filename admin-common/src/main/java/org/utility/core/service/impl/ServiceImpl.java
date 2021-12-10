package org.utility.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.utility.core.service.Service;

/**
 * Service 实现类（ 泛型：M 是 mapper 对象，T 是实体 ）
 *
 * @author Li Yanfeng
 */
public class ServiceImpl<M extends BaseMapper<T>, T> implements Service<T> {

    protected M baseMapper;

    @Autowired
    public void setBaseMapper(M baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public M getBaseMapper() {
        return baseMapper;
    }
}
