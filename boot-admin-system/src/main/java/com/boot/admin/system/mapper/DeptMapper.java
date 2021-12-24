package com.boot.admin.system.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.system.model.Dept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<Dept> selectDeptListByRoleId(@Param("roleId") Long roleId);
}
