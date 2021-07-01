package org.utility.modules.system.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 岗位 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class JobQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long jobId;
    /**
     * 岗位名称
     */
    @Query(type = Query.Type.LIKE)
    private String name;
    /**
     * 岗位状态
     */
    @Query(type = Query.Type.EQ)
    private Boolean enabled;
    /**
     * 时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
}
