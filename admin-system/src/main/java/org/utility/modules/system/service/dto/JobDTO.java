package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.base.BaseDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * 岗位 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class JobDTO extends BaseDTO implements Serializable {

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
    /**
     * 岗位状态
     */
    private Boolean enabled;
    /**
     * 排序
     */
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

    @Override
    public String toString() {
        return "JobDTO{" +
                "Long='" + jobId + '\'' +
                ", String='" + name + '\'' +
                ", Boolean='" + enabled + '\'' +
                ", Integer='" + jobSort + '\'' +
                '}';
    }
}
