package org.utility.modules.system.service.dto;

import org.utility.annotation.Query;
import org.utility.base.BaseQuery;


/**
 * 数据字典详情 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DictDetailQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long detailId;
    /**
     * 字典id
     */
    @Query(type = Query.Type.EQ)
    private Long dictId;
    /**
     * 字典标签
     */
    @Query(type = Query.Type.LIKE)
    private String label;


    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
