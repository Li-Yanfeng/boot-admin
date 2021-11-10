package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 数据字典详情
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_dict_detail")
public class DictDetail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "detail_id", type = IdType.ASSIGN_ID)
    private Long detailId;

    @ApiModelProperty(value = "字典id")
    private Long dictId;

    @ApiModelProperty(value = "字典标签")
    @NotBlank
    private String label;

    @ApiModelProperty(value = "字典值")
    @NotBlank
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort;


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
}
