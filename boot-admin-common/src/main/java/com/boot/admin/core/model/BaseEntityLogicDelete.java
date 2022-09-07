package com.boot.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 */
@ApiModel(description = "基础实体,包含通用实体字段")
public abstract class BaseEntityLogicDelete extends BaseEntity implements Serializable {

    @ApiModelProperty(value = "是否删除", hidden = true)
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
