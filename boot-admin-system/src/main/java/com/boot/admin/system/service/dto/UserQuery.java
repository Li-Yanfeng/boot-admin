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
 * @since 2021-06-01
 */
@Schema(description = "用户 数据查询对象")
public class UserQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long userId;

    @Schema(description = "部门ID")
    @Query(type = SqlKeyword.EQ)
    private Long deptId;

    @Schema(description = "部门ID")
    @Query(propName = "deptId", type = SqlKeyword.IN)
    private List<Long> deptIds;

    @Schema(description = "是否启用")
    @Query(type = SqlKeyword.EQ)
    private Integer enabled;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;

    @Schema(description = "多字段模糊")
    @Query(q = "email,username,nickName")
    private String q;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
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

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
