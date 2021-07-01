package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.base.BaseDTO;

import java.io.Serializable;

/**
 * 数据字典详情 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DictDetailDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long detailId;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 排序
     */
    private Integer dictSort;

    /**
     * 字典
     */
    private DictSmallDTO dict;


    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public DictSmallDTO getDict() {
        return dict;
    }

    public void setDict(DictSmallDTO dict) {
        this.dict = dict;
    }

    @Override
    public String toString() {
        return "DictDetailDTO{" +
                "detailId=" + detailId +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", dictSort=" + dictSort +
                ", dict=" + dict +
                '}';
    }
}
