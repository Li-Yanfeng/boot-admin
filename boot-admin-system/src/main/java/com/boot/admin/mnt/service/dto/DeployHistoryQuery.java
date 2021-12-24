package com.boot.admin.mnt.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "部署历史 数据查询对象")
public class DeployHistoryQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部署ID")
    @Query(type = SqlKeyword.EQ)
    private Long deployId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
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
