package org.utility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.model.AlipayConfig;

/**
 * 支付宝配置 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Repository
public interface AlipayMapper extends BaseMapper<AlipayConfig> {
}
