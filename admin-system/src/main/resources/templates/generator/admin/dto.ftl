package ${package}.service.dto;

<#if !auto && pkColumnType == 'Long'>
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
</#if>
<#if extendsSuperEntity>
import org.utility.core.service.dto.BaseDTO;
</#if>

<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
import java.util.Date;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>

/**
 * ${apiAlias!} 数据传输对象
 *
 * @author ${author}
 * @since ${date}
 */
<#if extendsSuperEntity>
public class ${className}DTO extends BaseDTO implements Serializable {
<#else>
public class ${className}DTO implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;

<#------------  BEGIN 字段循环遍历  ---------->
<#if columns??>
    <#list columns as column>
        <#if column.remark!?length gt 0>
    /**
     * ${column.remark}
     */
        </#if>
        <#if column.columnKey = 'PRI'>
            <#if !auto && pkColumnType = 'Long'>
    @JSONField(serializeUsing = ToStringSerializer.class)
            </#if>
        </#if>
        <#if column.changeColumnName != 'delFlag'>
    private ${column.columnType} ${column.changeColumnName};
        </#if>
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

    @Override
    public String toString() {
        return "${className}DTO{" +
<#list columns as column>
    <#if column_index==0>
                "${column.columnType}='" + ${column.changeColumnName} + '\'' +
    <#else>
                ", ${column.columnType}='" + ${column.changeColumnName} + '\'' +
    </#if>
</#list>
                '}';
    }
}
