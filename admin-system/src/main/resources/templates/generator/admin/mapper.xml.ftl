<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${moduleName}.mapper.${className}Mapper">

    <!--通用查询映射结果-->
    <resultMap id="BaseResultMap" type="${package}.${moduleName}.model.${className}">
<#list columns as column>
    <#if column.columnKey = 'PRI'><#--生成主键排在第一位-->
        <id column="${column.columnName}" property="${column.changeColumnName}"/>
    </#if>
</#list>
<#list columns as column>
    <#if column.columnKey != 'PRI'><#--生成普通字段 -->
        <result column="${column.columnName}" property="${column.changeColumnName}"/>
    </#if>
</#list>
<#if extendsSuperEntity>
    <#list commonColumns as column><#--生成公共字段 -->
        <result column="${column.columnName}" property="${column.changeColumnName}"/>
    </#list>
</#if>
    </resultMap>

    <!--通用查询结果列-->
    <sql id="Base_Column_List">
        <#list columns as column>${column.columnName}<#sep>, </#sep></#list><#if extendsSuperEntity>, <#list commonColumns as column>${column.columnName}<#sep>, </#sep></#list></#if>
    </sql>
</mapper>
