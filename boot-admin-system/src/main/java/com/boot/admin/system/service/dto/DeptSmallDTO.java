package com.boot.admin.system.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "部门 数据传输对象")
public class DeptSmallDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long deptId;

    @ApiModelProperty(value = "名称")
    private String name;


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeptSmallDTO() {
    }

    public DeptSmallDTO(Long deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
