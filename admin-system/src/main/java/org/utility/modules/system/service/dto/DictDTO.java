package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 数据字典 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DictDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dictId;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    /**
     * 字典详情
     */
    private List<DictDetailDTO> dictDetails;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DictDetailDTO> getDictDetails() {
        return dictDetails;
    }

    public void setDictDetails(List<DictDetailDTO> dictDetails) {
        this.dictDetails = dictDetails;
    }

    @Override
    public String toString() {
        return "DictDTO{" +
                "dictId=" + dictId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dictDetails=" + dictDetails +
                '}';
    }
}
