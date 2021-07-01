package org.utility.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.modules.system.model.DictDetail;

import java.util.List;

/**
 * 数据字典详情 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Repository
public interface DictDetailMapper extends BaseMapper<DictDetail> {

    /**
     * 根据 dictName 查询
     *
     * @param dictName 字典名称
     * @return /
     */
    @Select("SELECT dd.* FROM sys_dict_detail dd LEFT JOIN sys_dict d ON d.dict_id = dd.dict_id WHERE dict.name = #{dictName} ORDER BY dict_sort ASC")
    List<DictDetail> selectListByDictName(@Param("dictName") String dictName);
}
