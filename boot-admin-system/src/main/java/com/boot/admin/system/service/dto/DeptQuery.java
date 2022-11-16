package com.boot.admin.system.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "部门 数据查询对象")
public class DeptQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long deptId;

    @Schema(description = "上级部门")
    @Query(type = SqlKeyword.EQ)
    private Long pid;

    @Schema(description = "名称")
    @Query(type = SqlKeyword.LIKE)
    private String name;

    @Schema(description = "是否启用")
    @Query(propName = "is_enabled", type = SqlKeyword.EQ)
    private Integer enabled;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;

    @Schema(description = "排序字段")
    private List<String> sort;


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

    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public DeptQuery() {
    }

    public DeptQuery(Long pid) {
        this.pid = pid;
    }
}
