package org.utility.modules.system.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.core.service.dto.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 部门 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DeptQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long deptId;
    /**
     * 上级部门
     */
    @Query(type = Query.Type.EQ)
    private Long pid;
    /**
     * 上级部门为 null
     */
    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;
    /**
     * 名称
     */
    @Query(type = Query.Type.LIKE)
    private String name;
    /**
     * 状态
     */
    @Query
    private Boolean enabled;
    /**
     * 时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;


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

    public Boolean getPidIsNull() {
        return pidIsNull;
    }

    public void setPidIsNull(Boolean pidIsNull) {
        this.pidIsNull = pidIsNull;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }
}
