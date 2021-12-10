package org.utility.system.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "岗位 数据传输对象")
public class JobSmallDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long jobId;

    @ApiModelProperty(value = "岗位名称")
    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JobSmallDTO jobDTO = (JobSmallDTO) o;
        return Objects.equals(jobId, jobDTO.jobId) && Objects.equals(name, jobDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
