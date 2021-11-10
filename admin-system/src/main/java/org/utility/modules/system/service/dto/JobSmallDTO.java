package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.base.Objects;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;

/**
 * 岗位 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class JobSmallDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long jobId;
    /**
     * 岗位名称
     */
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
        if (this == o) return true;
        if (!(o instanceof JobSmallDTO)) return false;
        JobSmallDTO that = (JobSmallDTO) o;
        return Objects.equal(jobId, that.jobId) && Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jobId, name);
    }

    @Override
    public String toString() {
        return "JobSmallDTO{" +
                "jobId=" + jobId +
                ", name='" + name + '\'' +
                '}';
    }
}
