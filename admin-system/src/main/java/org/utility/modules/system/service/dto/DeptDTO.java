package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 部门 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DeptDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long deptId;
    /**
     * 上级部门
     */
    private Long pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 子部门数目
     */
    private Integer subCount;
    /**
     * 状态
     */
    private Boolean enabled;
    /**
     * 排序
     */
    private Integer deptSort;

    /**
     * 子部门
     */
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
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
        if (this == o) return true;
        if (!(o instanceof DeptDTO)) return false;
        DeptDTO deptDTO = (DeptDTO) o;
        return Objects.equal(deptId, deptDTO.deptId) && Objects.equal(name, deptDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deptId, name);
    }

    @Override
    public String toString() {
        return "DeptDTO{" +
                "deptId=" + deptId +
                ", pid=" + pid +
                ", subCount=" + subCount +
                ", name='" + name + '\'' +
                ", deptSort=" + deptSort +
                ", enabled=" + enabled +
                ", children=" + children +
                '}';
    }
}
