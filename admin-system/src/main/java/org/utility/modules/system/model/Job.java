package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 岗位
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_job")
public class Job extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "job_id", type = IdType.ASSIGN_ID)
    private Long jobId;

    @ApiModelProperty(value = "岗位名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "岗位状态")
    @NotNull
    private Boolean enabled;

    @ApiModelProperty(value = "排序")
    private Integer jobSort;


    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getJobSort() {
        return jobSort;
    }

    public void setJobSort(Integer jobSort) {
        this.jobSort = jobSort;
    }
}
