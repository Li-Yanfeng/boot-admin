package com.boot.admin.system.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.system.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据 menuId 查询
     *
     * @param menuId 菜单ID
     * @return 列表查询结果
     */
    List<Role> selectRoleListByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return 列表查询结果
     */
    List<Role> selectRoleListByUserId(@Param("userId") Long userId);
}
