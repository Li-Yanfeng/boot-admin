package org.utility.system.service;

import org.utility.core.service.Service;
import org.utility.system.model.Dept;
import org.utility.system.service.dto.DeptDTO;
import org.utility.system.service.dto.DeptQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 部门 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DeptService extends Service<Dept> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveDept(Dept resource);

    /**
     * 根据 DeptDTO 批量删除
     *
     * @param resources 数据传输对象列表
     */
    void removeDept(Collection<DeptDTO> resources);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateDeptById(Dept resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<DeptDTO> listDepts(DeptQuery query);

    /**
     * 根据 roleId 条件查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<DeptDTO> listDeptsByRoleId(Long roleId);

    /**
     * 获取所有子节点 (包含当前节点)
     *
     * @param childrenList 子节点列表
     * @param depts        节点列表
     * @return 列表查询结果
     */
    List<DeptDTO> listDeptsChildren(List<DeptDTO> childrenList, List<DeptDTO> depts);

    /**
     * 根据 ID 获取同级与上级数据
     *
     * @param resource 实体对象
     * @param results  已获取子节点列表
     * @return 列表查询结果
     */
    List<DeptDTO> listDeptsSuperior(DeptDTO resource, List<DeptDTO> results);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    DeptDTO getDeptById(Long id);

    /**
     * 构建部门树
     *
     * @param resources 数据传输对象列表
     * @return 操作结果
     */
    List<DeptDTO> buildTree(List<DeptDTO> resources);

    /**
     * 验证是否被关联
     *
     * @param ids 部门ID列表
     */
    void verification(Collection<Long> ids);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportDept(List<DeptDTO> exportData, HttpServletResponse response) throws IOException;
}
