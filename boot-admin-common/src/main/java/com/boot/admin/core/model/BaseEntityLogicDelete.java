package com.boot.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 */
@Schema(description = "基础实体,包含通用实体字段")
public abstract class BaseEntityLogicDelete extends BaseEntity implements Serializable {

    @Schema(description = "是否删除", hidden = true)
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer deleted;


    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
