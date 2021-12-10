package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.system.model.User;

import java.util.Collection;
import java.util.List;

/**
 * 用户 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 menuId 查询
     *
     * @param menuId 菜单ID
     * @return List<User>
     */
    List<User> selectUserListByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return List<User>
     */
    List<User> selectUserListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 deptId 查询
     *
     * @param deptId 部门ID
     * @return List<User>
     */
    List<User> selectUserListByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据 deptIds 统计
     *
     * @param deptIds 部门Id集合
     * @return /
     */
    int countUserIdByDeptIds(@Param("deptIds") Collection<Long> deptIds);
}
