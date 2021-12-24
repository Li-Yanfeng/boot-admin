package com.boot.admin.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.DictDetail;
import com.boot.admin.system.service.dto.DictDetailDTO;
import com.boot.admin.system.service.dto.DictDetailQuery;

import java.util.Collection;
import java.util.List;

/**
 * 字典详情 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DictDetailService extends Service<DictDetail> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveDictDetail(DictDetail resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeDictDetailByIds(Collection<Long> ids);

    /**
     * 根据 dictId 批量删除
     *
     * @param dictIds 主键ID列表
     */
    void removeDictDetailByDictIds(Collection<Long> dictIds);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateDictDetailById(DictDetail resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<DictDetailDTO> listDictDetails(DictDetailQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<DictDetailDTO> listDictDetails(DictDetailQuery query, Page<DictDetail> page);
}
