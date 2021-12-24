package com.boot.admin.system.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.system.model.Job;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @return 列表查询结果
     */
    List<Job> selectJobListByUserId(@Param("userId") Long userId);
}
