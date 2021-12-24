package ${package}.service.dto;

<#if (queryColumns?size > 0)>
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
</#if>
import io.swagger.annotations.ApiModel;
<#if (queryColumns?size > 0)>
import io.swagger.annotations.ApiModelProperty;
import Query;
</#if>

<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if (betweens?size > 0)>
import java.util.List;
</#if>
<#if queryHasTimestamp>
import java.sql.Timestamp;
</#if>
import java.io.Serializable;

/**
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${apiAlias!} 数据查询对象")
public class ${className}Query implements Serializable {

    private static final long serialVersionUID = 1L;
<#------------  BEGIN 字段循环遍历  ---------->
<#if queryColumns??>
    <#list queryColumns as column>

        <#if column.remark != ''>
    @ApiModelProperty(value = "${column.remark}")
        </#if>
        <#if column.queryType = 'In'>
    @Query(type = SqlKeyword.IN)
        <#elseif column.queryType = 'NotIn'>
    @Query(type = SqlKeyword.NOT_IN)
        <#elseif column.queryType = 'Like'>
    @Query(type = SqlKeyword.LIKE)
        <#elseif column.queryType = 'NotLike'>
    @Query(type = SqlKeyword.NOT_LIKE)
        <#elseif column.queryType = '='>
    @Query(type = SqlKeyword.EQ)
        <#elseif column.queryType = '!='>
    @Query(type = SqlKeyword.NE)
        <#elseif column.queryType = '>'>
    @Query(type = SqlKeyword.GT
        <#elseif column.queryType = '>='>
    @Query(type = SqlKeyword.GE)
        <#elseif column.queryType = '<'>
    @Query(type = SqlKeyword.LT)
        <#elseif column.queryType = '<='>
    @Query(type = SqlKeyword.LE)
        <#elseif column.queryType = 'IsNull'>
    @Query(type = SqlKeyword.IS_NULL)
        <#elseif column.queryType = 'NotNull'>
    @Query(type = SqlKeyword.IS_NOT_NULL)
        </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>

        <#if column.remark != ''>
    @ApiModelProperty(value = "${column.remark}")
        </#if>
    @Query(type = SqlKeyword.BETWEEN)
    private List<${column.columnType}> ${column.changeColumnName};
    </#list>
</#if>
<#------------  END 字段循环遍历  ---------->

<#if queryColumns??>
    <#list queryColumns as column>

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
<#if betweens??>
    <#list betweens as column>

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
