package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.base.BaseDTO;

import java.io.Serializable;

/**
 * 数据字典 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class DictSmallDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dictId;


    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    @Override
    public String toString() {
        return "DictSmallDTO{" +
                "dictId=" + dictId +
                '}';
    }
}
