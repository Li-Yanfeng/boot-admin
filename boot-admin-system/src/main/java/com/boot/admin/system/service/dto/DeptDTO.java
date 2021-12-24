package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "部门 数据传输对象")
public class DeptDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long deptId;

    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "子部门数目")
    private Integer subCount;

    @ApiModelProperty(value = "是否启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer deptSort;

    @ApiModelProperty(value = "子部门")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDTO> children;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubCount() {
        return subCount;
    }

    public void setSubCount(Integer subCount) {
        this.subCount = subCount;
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

    public List<DeptDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DeptDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeptDTO deptDTO = (DeptDTO) o;
        return Objects.equals(deptId, deptDTO.deptId) && Objects.equals(name, deptDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptId, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
