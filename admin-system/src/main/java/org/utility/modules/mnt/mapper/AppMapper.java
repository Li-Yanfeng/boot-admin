package org.utility.modules.mnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.modules.mnt.model.App;

/**
 * 应用管理 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Repository
public interface AppMapper extends BaseMapper<App> {
}
