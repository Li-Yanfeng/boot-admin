package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户岗位关联
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_user_job")
public class UserJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty(value = "岗位ID")
    @TableField(value = "job_id")
    private Long jobId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
