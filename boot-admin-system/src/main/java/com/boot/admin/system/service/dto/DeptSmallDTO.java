package com.boot.admin.system.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "部门 数据传输对象")
public class DeptSmallDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long deptId;

    @Schema(description = "名称")
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
