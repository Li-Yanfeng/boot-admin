package org.utility.modules.mnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.modules.mnt.model.Database;

/**
 * 数据库管理 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Repository
public interface DatabaseMapper extends BaseMapper<Database> {
}
