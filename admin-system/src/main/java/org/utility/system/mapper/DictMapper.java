package org.utility.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.utility.system.model.Dict;

/**
 * 数据字典 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface DictMapper extends BaseMapper<Dict> {
}
