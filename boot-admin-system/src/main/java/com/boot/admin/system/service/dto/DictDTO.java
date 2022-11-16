package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "字典 数据传输对象")
public class DictDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long dictId;

    @Schema(description = "字典名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "描述")
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
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
