package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.system.model.UserJob;

/**
 * 用户岗位关联 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface UserJobMapper extends BaseMapper<UserJob> {
}
