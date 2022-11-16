package com.boot.admin.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 */
@Schema(description = "基础实体,包含通用实体字段")
public abstract class BaseEntity implements Serializable {

    @Schema(description = "创建人", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    protected Long createBy;

    @Schema(description = "更新人", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updateBy;

    @Schema(description = "创建人名称", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    protected String createByName;

    @Schema(description = "更新人名称", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String updateByName;

    @Schema(description = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @Schema(description = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
}
