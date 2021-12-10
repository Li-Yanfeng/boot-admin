package org.utility.mnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.mnt.model.DeployHistory;

/**
 * 部署历史 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface DeployHistoryMapper extends BaseMapper<DeployHistory> {
}
