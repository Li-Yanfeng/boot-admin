package org.utility.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.utility.base.Service;
import org.utility.util.ConvertUtils;
import org.utility.util.QueryHelp;
import org.utility.util.ValidationUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    protected Class<T> entityClass = currentModelClass();

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
        return ConvertUtils.convertList(
                Optional.ofNullable(baseMapper.selectList(QueryHelp.queryWrapper(query))).orElseGet(ListUtil::empty),
                dtoClass
        );
    }

    @Override
    public IPage<D> page(Q query) {
        return ConvertUtils.convertPage(
                Optional.ofNullable(baseMapper.selectPage(QueryHelp.page(query), QueryHelp.queryWrapper(query))).orElseGet(Page::new),
                dtoClass
        );
    }

    @Override
    public D getById(Long id) {
        T t = baseMapper.selectById(id);
        ValidationUtils.notNull(t, entityClass.getSimpleName(), "id", id);
        return ConvertUtils.convert(t, dtoClass);
    }

    @Override
    public D getOne(Q query) {
        return ConvertUtils.convert(
                Optional.ofNullable(baseMapper.selectOne(QueryHelp.queryWrapper(query))).orElseGet(this::newInstance),
                dtoClass
        );
    }


    @Override
    public M getBaseMapper() {
        return baseMapper;
    }

    public T newInstance() {
        try {
            return entityClass.newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    protected Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 3);
    }
}
