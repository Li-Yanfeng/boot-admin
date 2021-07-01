package org.utility.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 本地存储 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public class LocalStorageQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "name,suffix,type,createBy,size")
    private String blurry;


    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }
}
