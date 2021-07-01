package org.utility.modules.mnt.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;
import org.utility.base.BaseQuery;

import java.util.Date;
import java.util.List;


/**
 * 应用管理 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class AppQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @Query(type = Query.Type.LIKE)
    private String name;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }
}
