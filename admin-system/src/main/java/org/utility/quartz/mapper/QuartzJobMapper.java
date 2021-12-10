package org.utility.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.quartz.model.QuartzJob;

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
     * @return /
     */
    @Select("SELECT * FROM sys_quartz_job WHERE is_pause = 0")
    List<QuartzJob> selectListByPauseIsFalse();
}
