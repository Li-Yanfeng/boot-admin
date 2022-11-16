package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "部门 数据传输对象")
public class DeptDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long deptId;

    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否启用")
    private Integer enabled;

    @Schema(description = "排序")
    private Integer deptSort;

    @Schema(description = "子部门")
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


    public DeptDTO() {
    }

    public DeptDTO(Long deptId, Long pid, String name, Integer enabled, Integer deptSort) {
        this.deptId = deptId;
        this.pid = pid;
        this.name = name;
        this.enabled = enabled;
        this.deptSort = deptSort;
    }
}
