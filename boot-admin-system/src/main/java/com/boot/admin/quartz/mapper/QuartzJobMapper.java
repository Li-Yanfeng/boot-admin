package com.boot.admin.quartz.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.quartz.model.QuartzJob;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定时任务 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    /**
     * 查询启用的任务
     *
     * @return 列表查询结果
     */
    @Select("SELECT * FROM sys_quartz_job WHERE is_pause = 0")
    List<QuartzJob> selectListByPauseIsFalse();
}
