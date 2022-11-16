package ${package}.service.dto;

<#if extendSuperEntity>
import com.boot.admin.core.service.dto.BaseDTO;
</#if>
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
 * @since ${date}
 */
@Schema(description = "${apiAlias!} 数据传输对象")
<#if extendSuperEntity>
public class ${className}DTO extends BaseDTO implements Serializable {
<#else>
public class ${className}DTO implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;
<#------------  BEGIN 字段循环遍历  ---------->
<#if columns??>
    <#list columns as column>

        <#if column.remark != ''>
    @Schema(description = "${column.remark}")
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
