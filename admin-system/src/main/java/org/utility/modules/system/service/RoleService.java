package org.utility.modules.system.service;

import org.springframework.security.core.GrantedAuthority;
import org.utility.core.service.Service;
import org.utility.modules.system.model.Role;
import org.utility.modules.system.service.dto.RoleDTO;
import org.utility.modules.system.service.dto.RoleQuery;
import org.utility.modules.system.service.dto.RoleSmallDTO;
import org.utility.modules.system.service.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 角色 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface RoleService extends Service<RoleDTO, RoleQuery, Role> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param resource 实体对象
     */
    void save(RoleDTO resource);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateById(RoleDTO resource);

    /**
     * 修改绑定的菜单
     *
     * @param resource /
     */
    void updateMenu(RoleDTO resource);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return /
     */
    List<RoleSmallDTO> listByUsersId(Long userId);

    /**
     * 根据角色查询角色级别
     *
     * @param roleIds /
     * @return /
     */
    Integer getLevelByRoles(Set<Long> roleIds);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user);

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    void verification(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<RoleDTO> queryAll) throws IOException;
}
