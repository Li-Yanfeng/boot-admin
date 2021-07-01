package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.base.Objects;
import org.utility.base.BaseDTO;

import java.io.Serializable;

/**
 * 部门 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DeptSmallDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long deptId;
    /**
     * 名称
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeptSmallDTO)) return false;
        DeptSmallDTO deptDTO = (DeptSmallDTO) o;
        return Objects.equal(deptId, deptDTO.deptId) && Objects.equal(name, deptDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deptId, name);
    }

    @Override
    public String toString() {
        return "DeptSmallDTO{" +
                "deptId=" + deptId +
                ", name='" + name + '\'' +
                '}';
    }
}
