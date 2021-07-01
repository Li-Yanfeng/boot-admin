package org.utility.modules.system.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 角色 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class RoleQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long roleId;
    /**
     * 名称
     */
    @Query(type = Query.Type.LIKE)
    private String name;
    /**
     * 时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
    /**
     * 多字段模糊
     */
    @Query(blurry = "name,description")
    private String blurry;

    @Override
    public String toString() {
        return "RoleQuery{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", blurry='" + blurry + '\'' +
                '}';
    }
}
