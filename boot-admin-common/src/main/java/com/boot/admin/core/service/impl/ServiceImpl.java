package com.boot.admin.core.service.impl;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.core.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
