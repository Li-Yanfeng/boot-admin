package org.utility.system.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "菜单 数据查询对象")
public class MenuQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long menuId;

    @ApiModelProperty(value = "上级菜单ID")
    @Query(type = SqlKeyword.EQ)
    private Long pid;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<Date> createTime;

    @ApiModelProperty(value = "多字段模糊")
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

    public MenuQuery() {
    }

    public MenuQuery(Long pid) {
        this.pid = pid;
    }
}
