package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.Menu;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色Id
     * @return /
     */
    @Select("SELECT m.* FROM sys_role_menu rm INNER JOIN sys_menu m ON m.menu_id = rm.menu_id WHERE rm.role_id = #{roleId}")
    Set<Menu> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 roleIds 查询
     *
     * @param roleIds 角色Id集合
     * @return /
     */
    @Select("<script>SELECT m.* FROM sys_menu m, sys_role_menu rm WHERE m.menu_id = rm.menu_id AND rm.role_id IN <foreach item='item' index='index' collection='roleIds' open='(' separator=',' close=')'> #{item} </foreach></script>")
    List<Menu> selectByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据 roleIds、type 查询
     *
     * @param roleIds 角色Id集合
     * @param type    /
     * @return /
     */
    @Select("<script>SELECT m.* FROM sys_menu m LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id LEFT JOIN sys_role r ON r.role_id = rm.role_id WHERE r.role_id IN <foreach item='item' index='index' collection='roleIds' open='(' separator=',' close=')'> #{item} </foreach> AND m.type &lt;&gt; #{type} ORDER BY m.menu_sort ASC</script>")
    LinkedHashSet<Menu> selectByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Long type);
}
