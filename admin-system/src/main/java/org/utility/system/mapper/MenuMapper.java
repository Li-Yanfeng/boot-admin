package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.system.model.Menu;

import java.util.LinkedHashSet;
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
     * @return Set<Menu>
     */
    Set<Menu> selectMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据 roleIds 查询
     *
     * @param roleIds 角色ID列表
     * @return Set<Menu>
     */
    Set<Menu> selectMenuListByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据 roleIds 和 type 查询
     *
     * @param roleIds 角色ID列表
     * @param type    菜单类型
     * @return LinkedHashSet<Menu>
     */
    LinkedHashSet<Menu> selectMenuListByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Long type);
}
