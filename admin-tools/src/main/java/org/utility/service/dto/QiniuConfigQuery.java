package org.utility.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 七牛云存储 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public class QiniuConfigQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    @Query(type = Query.Type.LIKE)
    private String key;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }
}
