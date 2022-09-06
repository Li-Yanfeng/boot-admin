package com.boot.admin.system.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.system.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户 Mapper 接口
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 menuId 查询
     *
     * @param menuId 菜单ID
     * @return 列表查询结果
     */
    List<User> selectUserListByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<User> selectUserListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 deptId 查询
     *
     * @param deptId 部门ID
     * @return 列表查询结果
     */
    List<User> selectUserListByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据 deptIds 统计
     *
     * @param deptIds 部门Id集合
     * @return 统计结果
     */
    int countUserIdByDeptIds(@Param("deptIds") Collection<Long> deptIds);
}
