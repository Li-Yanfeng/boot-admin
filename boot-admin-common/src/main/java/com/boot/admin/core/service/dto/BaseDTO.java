package com.boot.admin.core.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 浅析VO、DTO、DO、PO的概念、区别和用处
 *
 * @author Li Yanfeng
 * @link https://www.cnblogs.com/qixuejia/p/4390086.html
 */
@Schema(description = "基础数据传输对象,包含通用实体字段")
public abstract class BaseDTO implements Serializable {

    @Schema(description = "创建人")
    protected Long createBy;

    @Schema(description = "更新人")
    protected Long updateBy;

    @Schema(description = "创建人名称")
    protected String createByName;

    @Schema(description = "更新人名称")
    protected String updateByName;

    @Schema(description = "创建时间")
    protected LocalDateTime createTime;

    @Schema(description = "更新时间")
    protected LocalDateTime updateTime;


    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
