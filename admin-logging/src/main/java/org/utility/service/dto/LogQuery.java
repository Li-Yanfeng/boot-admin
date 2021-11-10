package org.utility.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.util.Date;
import java.util.List;

/**
 * 日志 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-04-15
 */
public class LogQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    @Query(type = Query.Type.EQ)
    private String logType;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;


    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
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
