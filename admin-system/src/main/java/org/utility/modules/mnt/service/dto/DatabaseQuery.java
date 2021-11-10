package org.utility.modules.mnt.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 数据库管理 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class DatabaseQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * jdbc连接
     */
    @Query(type = Query.Type.EQ)
    private String jdbcUrl;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "name,userName,jdbcUrl")
    private String blurry;


    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

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
