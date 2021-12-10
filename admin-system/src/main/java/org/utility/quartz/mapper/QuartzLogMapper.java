package org.utility.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.quartz.model.QuartzLog;

/**
 * 定时任务日志 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
}
