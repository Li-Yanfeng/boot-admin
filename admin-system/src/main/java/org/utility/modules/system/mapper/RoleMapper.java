package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return /
     */
    @Select("SELECT r.* FROM sys_role r, sys_user_role ur WHERE r.role_id = ur.role_id AND ur.user_id = #{userId}")
    Set<Role> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据 menuIds 查询
     *
     * @param menuIds 菜单Id集合
     * @return /
     */
    @Select("<script>SELECT r.* FROM sys_role r, sys_role_menu rm WHERE r.role_id = rm.role_id AND rm.menu_id IN <foreach item='item' index='index' collection='menuIds' open='(' separator=',' close=')'> #{item} </foreach></script>")
    List<Role> selectByMenuIds(@Param("menuIds") List<Long> menuIds);
}
