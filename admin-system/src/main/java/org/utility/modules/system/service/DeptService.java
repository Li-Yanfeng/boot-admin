package org.utility.modules.system.service;

import org.utility.core.service.Service;
import org.utility.modules.system.model.Dept;
import org.utility.modules.system.service.dto.DeptDTO;
import org.utility.modules.system.service.dto.DeptQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface DeptService extends Service<DeptDTO, DeptQuery, Dept> {

    /**
     * 根据 pid 查询
     *
     * @param pid 上级部门ID
     * @return /
     */
    List<Dept> listByPid(Long pid);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return /
     */
    Set<DeptDTO> listByRoleId(Long roleId);

    /**
     * 获取子部门
     *
     * @param deptId   部门Id
     * @param deptList 部门集合
     * @return /
     */
    List<Long> listDeptChildren(Long deptId, List<Dept> deptList);

    /**
     * 获取待删除的部门
     *
     * @param deptList 部门集合
     * @param deptIds  部门ID集合
     * @return
     */
    Set<Long> listDeleteDept(List<Dept> deptList, Set<Long> deptIds);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param deptDto /
     * @param depts   /
     * @return /
     */
    List<DeptDTO> getSuperior(DeptDTO deptDto, List<Dept> depts);

    /**
     * 构建树形数据
     *
     * @param deptDtos /
     * @return /
     */
    Map<String, Object> buildTree(List<DeptDTO> deptDtos);

    /**
     * 验证是否被角色或用户关联
     *
     * @param deptIds 部门Id集合
     */
    void verification(Set<Long> deptIds);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<DeptDTO> queryAll) throws IOException;
}
