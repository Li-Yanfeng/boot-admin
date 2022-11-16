package com.boot.admin.quartz.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "定时任务 数据查询对象")
public class QuartzJobQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务名称")
    @Query(type = SqlKeyword.LIKE)
    private String jobName;

    @Schema(description = "是否成功")
    @Query(type = SqlKeyword.EQ)
    private Integer success;

    @Schema(description = "创建时间")
    @Query(type = SqlKeyword.BETWEEN)
    private List<Timestamp> createTime;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Timestamp> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Timestamp> createTime) {
        this.createTime = createTime;
    }
}
