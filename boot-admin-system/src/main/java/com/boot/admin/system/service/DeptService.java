package com.boot.admin.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.Dept;
import com.boot.admin.system.service.dto.DeptDTO;
import com.boot.admin.system.service.dto.DeptQuery;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 部门 服务类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
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
     * @param id 主键ID
     * @return 列表查询结果
     */
    default List<DeptDTO> listDeptsChildren(Long id) {
        return listDeptsChildren(id, true);
    }

    /**
     * 获取所有子节点
     *
     * @param id             主键ID
     * @param containsItself 包含自己
     * @return 列表查询结果
     */
    List<DeptDTO> listDeptsChildren(Long id, boolean containsItself);

    /**
     * 根据 ID 获取上级数据
     *
     * @param id 主键ID
     * @return 列表查询结果
     */
    List<DeptDTO> listDeptsSuperior(Long id);

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
    List<Tree<Long>> buildTree(Collection<DeptDTO> resources);

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
     */
    void exportDept(List<DeptDTO> exportData, HttpServletResponse response);
}
