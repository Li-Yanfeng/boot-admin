package com.boot.admin.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Li Yanfeng
 */
@Schema(description = "日志 数据查询对象")
public class LogQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志类型")
    @Query(type = SqlKeyword.EQ)
    private String logType;

    @Schema(description = "创建人")
    @Query(type = SqlKeyword.EQ)
    private Long createBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;

    @Schema(description = "多字段模糊")
    @Query(q = "createByName,description,address,requestIp,method,params")
    private String q;

    @Schema(description = "排序字段")
    private List<String> sort;


    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }
}
