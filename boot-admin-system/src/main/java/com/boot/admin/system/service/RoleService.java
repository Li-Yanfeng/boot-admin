package com.boot.admin.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.Role;
import com.boot.admin.system.service.dto.RoleDTO;
import com.boot.admin.system.service.dto.RoleQuery;
import com.boot.admin.system.service.dto.RoleSmallDTO;
import com.boot.admin.system.service.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 角色 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface RoleService extends Service<Role> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveRole(RoleDTO resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeRoleByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateRoleById(RoleDTO resource);

    /**
     * 修改绑定的菜单
     *
     * @param newResource 待修改的数据
     * @param oldResource 之前存在的数据
     */
    void updateMenu(RoleDTO newResource, RoleDTO oldResource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<RoleDTO> listRoles(RoleQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<RoleDTO> listRoles(RoleQuery query, Page<Role> page);

    /**
     * 根据  userId 查询
     *
     * @param userId 菜单ID列表
     * @return 列表查询结果
     */
    List<RoleSmallDTO> listRolesByUserId(Long userId);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    RoleDTO getRoleById(Long id);

    /**
     * 根据 roleIds 查询角色级别
     *
     * @param roleIds /
     * @return /
     */
    Integer getLevelByRoles(Collection<Long> roleIds);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user);

    /**
     * 验证是否被关联
     *
     * @param ids 角色ID列表
     */
    void verification(Collection<Long> ids);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     */
    void exportRole(List<RoleDTO> exportData, HttpServletResponse response);
}
