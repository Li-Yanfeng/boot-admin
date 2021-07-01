package org.utility.modules.system.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 菜单 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class MenuQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long menuId;
    /**
     * 上级菜单ID
     */
    @Query(type = Query.Type.EQ)
    private Long pid;
    /**
     * 上级菜单ID 为 null
     */
    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;
    /**
     * 时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "title,component,permission")
    private String blurry;


    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
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
