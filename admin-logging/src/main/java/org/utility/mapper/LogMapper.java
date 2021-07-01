package org.utility.mapper;

import org.springframework.stereotype.Repository;
import org.utility.model.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统日志 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-04-15
 */
@Repository
public interface LogMapper extends BaseMapper<Log> {
}
