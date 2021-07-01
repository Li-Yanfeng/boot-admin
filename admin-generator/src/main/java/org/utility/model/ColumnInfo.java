package org.utility.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.util.GenUtils;

import java.io.Serializable;

/**
 * 列的数据信息
 *
 * @author Li Yanfeng
 */
@TableName(value = "code_column_config")
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(value = "column_id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "数据库字段名称")
    private String columnName;

    @ApiModelProperty(value = "数据库字段类型")
    private String columnType;

    @ApiModelProperty(value = "数据库字段键类型")
    private String keyType;

    @ApiModelProperty(value = "字段额外的参数")
    private String extra;

    @ApiModelProperty(value = "数据库字段描述")
    private String remark;

    @ApiModelProperty(value = "是否必填")
    private Boolean notNull;

    @ApiModelProperty(value = "是否在列表显示")
    private Boolean listShow = true;

    @ApiModelProperty(value = "是否表单显示")
    private Boolean formShow = true;

    @ApiModelProperty(value = "表单类型")
    private String formType;

    @ApiModelProperty(value = "查询 1:模糊 2：精确")
    private String queryType;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "日期注解")
    private String dateAnnotation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
        if (GenUtils.PK.equalsIgnoreCase(keyType) && GenUtils.EXTRA.equalsIgnoreCase(extra)) {
            this.notNull = false;
        }
    }

    public Boolean getListShow() {
        return listShow;
    }

    public void setListShow(Boolean listShow) {
        this.listShow = listShow;
    }

    public Boolean getFormShow() {
        return formShow;
    }

    public void setFormShow(Boolean formShow) {
        this.formShow = formShow;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDateAnnotation() {
        return dateAnnotation;
    }

    public void setDateAnnotation(String dateAnnotation) {
        this.dateAnnotation = dateAnnotation;
    }
}
