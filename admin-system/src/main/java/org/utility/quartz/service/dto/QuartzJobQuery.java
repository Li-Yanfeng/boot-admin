package org.utility.quartz.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.utility.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "定时任务 数据查询对象")
public class QuartzJobQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称")
    @Query(type = SqlKeyword.LIKE)
    private String jobName;

    @ApiModelProperty(value = "是否成功")
    @Query(type = SqlKeyword.EQ)
    private Integer success;

    @ApiModelProperty(value = "创建时间")
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
