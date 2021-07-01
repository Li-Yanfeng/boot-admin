package org.utility.modules.mnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.modules.mnt.model.DeployServer;

/**
 * 应用与服务器关联 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Repository
public interface DeployServerMapper extends BaseMapper<DeployServer> {
}
