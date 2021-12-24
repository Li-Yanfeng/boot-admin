package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "字典 数据传输对象")
public class DictSmallDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long dictId;


    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
