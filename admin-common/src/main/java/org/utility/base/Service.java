package org.utility.base;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Collection;
import java.util.List;

/**
 * 通用 Service 接口
 *
 * @param <D> DTO 数据传输对象
 * @param <Q> Query 数据查询对象
 * @param <T> Entity 实体
 * @author Li Yanfeng
 */
public interface Service<D, Q, T> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param resource 实体对象
     */
    void save(T resource);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    void removeById(Long id);

    /**
     * 根据 query 条件，删除记录
     *
     * @param query 数据查询对象
     */
    void remove(Q query);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param ids 主键ID列表
     */
    void removeByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateById(T resource);

    /**
     * 查询（根据 query 条件）
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<D> list(Q query);

    /**
     * 翻页查询（根据 query 条件）
     *
     * @param query 数据查询对象
     * @return 翻页查询结果
     */
    IPage<D> page(Q query);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    D getById(Long id);

    /**
     * 根据 query 条件 查询一条记录
     *
     * @param query 数据查询对象
     * @return 实体对象
     */
    D getOne(Q query);
}
