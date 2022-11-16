package com.boot.admin.system.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "字典 数据查询对象")
public class DictQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long dictId;

    @Schema(description = "字典名称")
    @Query(type = SqlKeyword.LIKE)
    private String name;

    @Schema(description = "多字段模糊")
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
