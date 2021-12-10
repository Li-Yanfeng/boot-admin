package org.utility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.utility.model.QiniuContent;

/**
 * 七牛云文件 Mapper 接口
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Repository
public interface QiniuContentMapper extends BaseMapper<QiniuContent> {

    /**
     * 根据 Key 查询
     *
     * @param key 文件名
     * @return /
     */
    @Select("SELECT content_id, bucket, name, size, type , url, suffix, update_time FROM tool_qiniu_content WHERE name = #{key}")
    QiniuContent selectByKey(@Param("key") String key);
}
