package com.boot.admin.system.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "字典详情 数据查询对象")
public class DictDetailQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @Query(type = SqlKeyword.EQ)
    private Long detailId;

    @Schema(description = "字典id")
    @Query(type = SqlKeyword.EQ)
    private Long dictId;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典标签")
    @Query(type = SqlKeyword.LIKE)
    private String label;

    @Schema(description = "排序字段")
    private List<String> sort;


    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }
}
