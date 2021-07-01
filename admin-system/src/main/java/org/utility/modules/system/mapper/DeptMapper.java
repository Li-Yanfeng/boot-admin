package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.Dept;

import java.util.Set;

/**
 * 部门 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return /
     */
    @Select("SELECT d.* FROM sys_dept d LEFT JOIN sys_role_dept rd ON d.dept_id = rd.dept_id WHERE rd.role_id = #{roleId}")
    Set<Dept> selectByRoleId(@Param("roleId") Long roleId);
}
