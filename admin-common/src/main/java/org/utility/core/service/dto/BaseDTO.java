package org.utility.core.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 浅析VO、DTO、DO、PO的概念、区别和用处
 *
 * @author Li Yanfeng
 * @link https://www.cnblogs.com/qixuejia/p/4390086.html
 */
@ApiModel(description = "基础数据传输对象,包含通用实体字段")
public abstract class BaseDTO implements Serializable {

    @ApiModelProperty(value = "创建人")
    protected String createBy;

    @ApiModelProperty(value = "更新人")
    protected String updateBy;

    @ApiModelProperty(value = "创建时间")
    protected Date createTime;

    @ApiModelProperty(value = "更新时间")
    protected Date updateTime;


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
