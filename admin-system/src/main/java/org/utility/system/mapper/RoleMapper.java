package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.system.model.Role;

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
     * @return List<Role>
     */
    List<Role> selectRoleListByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return List<Role>
     */
    List<Role> selectRoleListByUserId(@Param("userId") Long userId);
}
