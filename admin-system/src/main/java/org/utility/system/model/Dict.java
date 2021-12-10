package org.utility.system.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;
import org.utility.core.validation.Update;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "数据字典")
@TableName(value = "sys_dict")
public class Dict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long dictId;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;


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
}
