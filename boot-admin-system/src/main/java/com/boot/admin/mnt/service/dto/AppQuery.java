package com.boot.admin.mnt.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "应用 数据查询对象")
public class AppQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应用名称")
    @Query(type = SqlKeyword.LIKE)
    private String name;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
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
