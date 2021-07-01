package org.utility.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.utility.base.BaseService;
import org.utility.util.QueryHelp;

import java.util.Collection;
import java.util.List;

/**
 * Service 实现类
 *
 * @param <M> Mapper 接口
 * @param <Q> Query 数据查询对象
 * @param <T> Entity 实体
 * @author Li Yanfeng
 */
public class BaseServiceImpl<M extends BaseMapper<T>, Q, T> implements BaseService<Q, T> {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    protected M baseMapper;

    @Override
    public void save(T resource) {
        baseMapper.insert(resource);
    }

    @Override
    public void removeById(Long id) {
        if (ObjectUtil.isNotNull(id)) {
            this.removeByIds(CollUtil.newHashSet(id));
        }
    }

    @Override
    public void remove(Q query) {
        baseMapper.delete(QueryHelp.queryWrapper(query));
    }

    @Override
    public void removeByIds(Collection<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            baseMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void updateById(T resource) {
        baseMapper.updateById(resource);
    }

    @Override
    public List<T> list(Q query) {
        return baseMapper.selectList(QueryHelp.queryWrapper(query));
    }

    @Override
    public IPage<T> page(Q query) {
        return baseMapper.selectPage(QueryHelp.page(query), QueryHelp.queryWrapper(query));
    }

    @Override
    public T getById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        return baseMapper.selectById(id);
    }

    @Override
    public T getOne(Q query) {
        return baseMapper.selectOne(QueryHelp.queryWrapper(query));
    }
}
