package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.UserRole;

/**
 * 用户角色关联 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
