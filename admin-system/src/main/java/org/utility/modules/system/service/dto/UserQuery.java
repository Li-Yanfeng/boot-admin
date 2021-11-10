package org.utility.modules.system.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 用户 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class UserQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long userId;
    /**
     * 部门名称
     */
    private Long deptId;
    /**
     * 状态：1启用、0禁用
     */
    @Query(type = Query.Type.EQ)
    private Long enabled;
    /**
     * 部门 Id
     */
    @Query(type = Query.Type.IN, propName = "dept_id")
    private Set<Long> deptIds = new HashSet<>();
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "email,username,nickName")
    private String blurry;


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

    public Long getEnabled() {
        return enabled;
    }

    public void setEnabled(Long enabled) {
        this.enabled = enabled;
    }

    public Set<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(Set<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }
}
