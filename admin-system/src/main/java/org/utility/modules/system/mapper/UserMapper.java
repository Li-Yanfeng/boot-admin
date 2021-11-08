package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.User;

import java.util.List;
import java.util.Set;

/**
 * 用户 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色Id
     * @return /
     */
    @Select("SELECT u.* FROM sys_user u, sys_user_role ur WHERE u.user_id = ur.user_id AND ur.role_id = #{roleId}")
    List<User> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 menuId 查询
     *
     * @param menuId 菜单ID
     * @return /
     */
    @Select("SELECT u.* FROM sys_user u, sys_user_role ur, sys_role_menu rm WHERE u.user_id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = #{menuId} GROUP BY u.user_id")
    List<User> selectByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据 deptId 查询
     *
     * @param deptId 部门Id
     * @return /
     */
    @Select("SELECT u.* FROM sys_user u, sys_user_role ur, sys_role_dept rd WHERE u.user_id = ur.user_id AND ur.role_id = rd.role_id AND rd.dept_id = #{deptId} GROUP BY u.user_id")
    List<User> selectByDeptId(Long deptId);

    /**
     * 根据 roleIds 查询
     *
     * @param deptIds 部门Id集合
     * @return /
     */
    @Select("<script>SELECT count(1) FROM sys_user u WHERE u.dept_id IN <foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'> #{item} </foreach></script>")
    int countByDeptIds(@Param("ids") Set<Long> deptIds);
}
