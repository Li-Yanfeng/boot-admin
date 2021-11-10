package org.utility.modules.quartz.service.dto;

import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.sql.Timestamp;
import java.util.List;


/**
 * 定时任务 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public class QuartzJobQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    @Query(type = Query.Type.LIKE)
    private String jobName;
    /**
     * 状态
     */
    @Query(type = Query.Type.EQ)
    private Boolean success;
    /**
     * 时间
     */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Timestamp> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Timestamp> createTime) {
        this.createTime = createTime;
    }
}
