package org.utility.system.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;
import org.utility.core.validation.Update;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "岗位")
@TableName(value = "sys_job")
public class Job extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long jobId;

    @ApiModelProperty(value = "岗位名称")
    private String name;

    @ApiModelProperty(value = "是否启用")
    @TableField(value = "is_enabled")
    private Integer enabled;

    @ApiModelProperty(value = "排序号")
    private Integer sort;


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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
