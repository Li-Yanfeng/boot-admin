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
@Schema(description = "菜单 数据查询对象")
public class MenuQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long menuId;

    @Schema(description = "上级菜单ID")
    @Query(type = SqlKeyword.EQ)
    private Long pid;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;

    @Schema(description = "多字段模糊")
    @Query(q = "title,component,permission")
    private String q;

    @Schema(description = "排序字段")
    private List<String> sort;


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

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public MenuQuery() {
    }

    public MenuQuery(Long pid) {
        this.pid = pid;
    }
}
