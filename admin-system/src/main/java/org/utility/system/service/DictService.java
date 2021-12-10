package org.utility.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.utility.core.service.Service;
import org.utility.system.model.Dict;
import org.utility.system.service.dto.DictDTO;
import org.utility.system.service.dto.DictQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DictService extends Service<Dict> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveDict(Dict resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeDictByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateDictById(Dict resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<DictDTO> listDicts(DictQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  分页参数对象
     * @return 翻页查询结果
     */
    Page<DictDTO> listDicts(DictQuery query, Page<Dict> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    DictDTO getDictById(Long id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportDict(List<DictDTO> exportData, HttpServletResponse response) throws IOException;
}
