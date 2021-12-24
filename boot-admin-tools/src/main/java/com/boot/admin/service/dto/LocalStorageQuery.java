package com.boot.admin.service.dto;

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
@ApiModel(description = "本地存储 数据查询对象")
public class LocalStorageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<Date> createTime;

    @ApiModelProperty(value = "多字段模糊")
    @Query(q = "name,suffix,type,createBy,size")
    private String q;


    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
