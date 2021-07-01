package org.utility.modules.mnt.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 部署历史管理 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class DeployHistoryQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 部署ID
     */
    @Query(type = Query.Type.EQ)
    private Long deployId;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;


    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }
}
