package com.boot.admin.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "部门")
@TableName(value = "sys_dept")
public class Dept extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long deptId;

    @Schema(description = "上级部门")
    @NotNull
    private Long pid;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否启用")
    @TableField(value = "is_enabled")
    private Integer enabled;

    @Schema(description = "排序")
    private Integer deptSort;


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getDeptSort() {
        return deptSort;
    }

    public void setDeptSort(Integer deptSort) {
        this.deptSort = deptSort;
    }
}
