package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 */
@Schema(description = "代码生成字段信息存储")
@TableName(value = "code_column_config")
public class ColumnConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long columnId;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "数据库字段名称")
    private String columnName;

    @Schema(description = "数据库字段类型")
    private String columnType;

    @Schema(description = "数据库字段键类型")
    private String keyType;

    @Schema(description = "字段额外的参数")
    private String extra;

    @Schema(description = "数据库字段描述")
    private String remark;

    @Schema(description = "是否必填")
    @TableField(value = "is_not_null")
    private Integer notNull;

    @Schema(description = "是否在列表显示")
    @TableField(value = "is_list_show")
    private Integer listShow;

    @Schema(description = "是否表单显示")
    @TableField(value = "is_form_show")
    private Integer formShow;

    @Schema(description = "表单类型")
    private String formType;

    @Schema(description = "查询类型")
    private String queryType;

    @Schema(description = "字典名称")
    private String dictName;


    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
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

    public Integer getNotNull() {
        return notNull;
    }

    public void setNotNull(Integer notNull) {
        this.notNull = notNull;
    }

    public Integer getListShow() {
        return listShow;
    }

    public void setListShow(Integer listShow) {
        this.listShow = listShow;
    }

    public Integer getFormShow() {
        return formShow;
    }

    public void setFormShow(Integer formShow) {
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
}
