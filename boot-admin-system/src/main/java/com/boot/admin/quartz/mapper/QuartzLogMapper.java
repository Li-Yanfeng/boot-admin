package com.boot.admin.quartz.mapper;

import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.quartz.model.QuartzLog;
import org.springframework.stereotype.Repository;

/**
 * 定时任务日志 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {
}
