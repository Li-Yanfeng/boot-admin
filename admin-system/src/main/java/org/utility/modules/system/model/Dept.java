package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_dept")
public class Dept extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "dept_id", type = IdType.ASSIGN_ID)
    private Long deptId;

    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "子部门数目")
    private Integer subCount;

    @ApiModelProperty(value = "状态")
    @NotNull
    private Boolean enabled;

    @ApiModelProperty(value = "排序")
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
}
