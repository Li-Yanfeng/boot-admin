package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.system.model.Job;

import java.util.Set;

/**
 * 岗位 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface JobMapper extends BaseMapper<Job> {

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return List<Job>
     */
    Set<Job> selectJobListByUserId(@Param("userId") Long userId);
}
