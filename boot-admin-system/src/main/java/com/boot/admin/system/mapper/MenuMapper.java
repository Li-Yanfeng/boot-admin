package com.boot.admin.system.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.system.model.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 菜单 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<Menu> selectMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 roleIds 查询
     *
     * @param roleIds 角色ID列表
     * @return 列表查询结果
     */
    List<Menu> selectMenuListByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据 roleIds 和 type 查询
     *
     * @param roleIds 角色ID列表
     * @param type    菜单类型
     * @return 列表查询结果
     */
    List<Menu> selectMenuListByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Long type);
}
