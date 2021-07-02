package ${package}.service.dto;

<#if (queryColumns?size > 0)>
import org.utility.annotation.Query;
</#if>
import org.utility.base.BaseQuery;

<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if (betweens?size > 0)>
import java.util.List;
</#if>
<#if queryHasTimestamp>
import java.sql.Timestamp;
</#if>

/**
 * ${apiAlias!} 数据查询对象
 *
 * @author ${author}
 * @since ${date}
 */
public class ${className}Query extends BaseQuery {

    private static final long serialVersionUID = 1L;

<#------------  BEGIN 字段循环遍历  ---------->
<#if queryColumns??>
    <#list queryColumns as column>
        <#if column.remark!?length gt 0>
    /**
     * ${column.remark}
     */
        </#if>
        <#if column.queryType = '='>
    @Query(type = Query.Type.EQ)
        </#if>
        <#if column.queryType = 'Like'>
    @Query(type = Query.Type.LIKE)
        </#if>
        <#if column.queryType = '!='>
    @Query(type = Query.Type.NE)
        </#if>
        <#if column.queryType = 'NotNull'>
    @Query(type = Query.Type.IS_NOT_NULL)
        </#if>
        <#if column.queryType = '>='>
    @Query(type = Query.Type.GT)
        </#if>
        <#if column.queryType = '<='>
    @Query(type = Query.Type.LT)
        </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
        <#if column.remark!?length gt 0>
    /**
     * ${column.remark}
     */
        </#if>
    @Query(type = Query.Type.BETWEEN)
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
