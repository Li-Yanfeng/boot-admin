package org.utility.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.modules.quartz.model.QuartzLog;

/**
 * 定时任务日志 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Repository
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
}
