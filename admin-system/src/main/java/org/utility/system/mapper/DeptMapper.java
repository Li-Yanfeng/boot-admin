package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.system.model.Dept;

import java.util.Set;

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
     * @return Set<Dept>
     */
    Set<Dept> selectDeptListByRoleId(Long roleId);
}
