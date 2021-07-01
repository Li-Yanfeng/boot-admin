package org.utility.modules.system.service.dto;

import org.utility.annotation.Query;
import org.utility.base.BaseQuery;


/**
 * 数据字典 数据查询对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DictQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Query(type = Query.Type.EQ)
    private Long dictId;
    /**
     * 字典名称
     */
    @Query(type = Query.Type.LIKE)
    private String name;
    /**
     * 多字段模糊
     */
    @Query(blurry = "name,description")
    private String blurry;


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

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }
}
