package com.boot.admin.system.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "字典 数据查询对象")
public class DictQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long dictId;

    @ApiModelProperty(value = "字典名称")
    @Query(type = SqlKeyword.LIKE)
    private String name;

    @ApiModelProperty(value = "多字段模糊")
    @Query(q = "name,description")
    private String q;


    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
