package org.utility.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.utility.base.Service;
import org.utility.util.ConvertUtils;
import org.utility.util.QueryHelp;

import java.util.Collection;
import java.util.List;

/**
 * Service 实现类
 *
 * @param <M> Mapper 接口
 * @param <D> DTO 数据传输对象
 * @param <Q> Query 数据查询对象
 * @param <T> Entity 实体
 * @author Li Yanfeng
 */
public class ServiceImpl<M extends BaseMapper<T>, D, Q, T> implements Service<D, Q, T> {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    protected M baseMapper;

    protected Class<D> dtoClass = currentDtoClass();

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
    public List<D> list(Q query) {
        return ConvertUtils.convertList(baseMapper.selectList(QueryHelp.queryWrapper(query)), dtoClass);
    }

    @Override
    public IPage<D> page(Q query) {
        return ConvertUtils.convertPage(baseMapper.selectPage(QueryHelp.page(query), QueryHelp.queryWrapper(query)), dtoClass);
    }

    @Override
    public D getById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        return ConvertUtils.convert(baseMapper.selectById(id), dtoClass);
    }

    @Override
    public D getOne(Q query) {
        return ConvertUtils.convert(baseMapper.selectOne(QueryHelp.queryWrapper(query)), dtoClass);
    }

    protected Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }
}
