package ${package}.model;

import com.baomidou.mybatisplus.annotation.*;
<#if extendSuperEntity>
    <#if extendLogicDeleteSuperEntity>
import com.boot.admin.core.model.BaseEntityLogicDelete;
    <#else>
import com.boot.admin.core.model.BaseEntity;
    </#if>
</#if>
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
import java.time.LocalDateTime;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>

/**
 * @author ${author}
 * @date ${date}
 */
@Schema(description = "${apiAlias!}")
@TableName(value = "${tableName}")
<#if extendSuperEntity>
    <#if extendLogicDeleteSuperEntity>
public class ${className} extends BaseEntityLogicDelete implements Serializable {
    <#else>
public class ${className} extends BaseEntity implements Serializable {
    </#if>
<#else>
public class ${className} implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;
<#------------  BEGIN 字段循环遍历  ---------->
<#if columns??>
    <#list columns as column>

        <#if column.remark != ''>
    @Schema(description = "${column.remark}")
        </#if>
        <#if column.columnKey = 'PRI'>
            <#if auto>
    @TableId(type = IdType.AUTO)
            <#else>
    @TableId(type = IdType.ASSIGN_ID)
            </#if>
            <#if column.columnType = 'String'>
    @NotBlank(message = "主键不能为空", groups = ValidGroup.Update.class)
            <#else>
    @NotNull(message = "主键不能为空", groups = ValidGroup.Update.class)
            </#if>
        </#if>
        <#if column.columnName?starts_with("is_")>
    @TableField(value = "${column.columnName}")
        </#if>
        <#if column.isNotNull && column.columnKey != 'PRI'>
            <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空")
            <#else>
    @NotNull(message = "${column.remark}不能为空")
            </#if>
        </#if>
        <#if !extendSuperEntity>
            <#if column.changeColumnName?starts_with('create')>
    @TableField(fill = FieldFill.INSERT)
            <#elseif column.changeColumnName?starts_with('update')>
    @TableField(fill = FieldFill.INSERT_UPDATE)
            </#if>
        </#if>
        <#if column.changeColumnName = 'deleted'>
    @TableLogic
        </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
<#------------  END 字段循环遍历  ---------->

<#if columns??>
    <#list columns as column>

        <#if column.columnType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${column.columnType} ${getprefix}${column.capitalColumnName}() {
        return ${column.changeColumnName};
    }

    public void set${column.capitalColumnName}(${column.columnType} ${column.changeColumnName}) {
        this.${column.changeColumnName} = ${column.changeColumnName};
    }
    </#list>
</#if>
}
