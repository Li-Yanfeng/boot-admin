package ${package}.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
import cn.hutool.core.util.ObjectUtil;
            <#break>
        </#if>
    </#list>
</#if>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.core.service.impl.ServiceImpl;
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
import com.boot.admin.exception.EntityExistException;
            <#break>
        </#if>
    </#list>
</#if>
import ${package}.mapper.${className}Mapper;
import ${package}.model.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}DTO;
import ${package}.service.dto.${className}Query;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ${apiAlias!} 服务实现类
 *
 * @author ${author}
 * @date ${date}
 */
@Service
@CacheConfig(cacheNames = {"${changeClassName}"})
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

    private final RedisUtils redisUtils;

    public ${className}ServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void save${className}(${className} resource) {
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
        if (ObjectUtil.isNotNull(lambdaQuery().eq(${className}::get${column.capitalColumnName}, resource.get${column.capitalColumnName}()).one())) {
            throw new EntityExistException(resource.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        baseMapper.insert(resource);
    }

    @Override
    public void remove${className}ByIds(Collection<${pkColumnType}> ids) {
        // 清理缓存
        redisUtils.delByKeys(CacheConstant.${cacheKey}_ID, ids);
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void update${className}ById(${className} resource) {
        ${pkColumnType} ${pkChangeColName} = resource.get${pkCapitalColName}();
        Assert.notNull(baseMapper.selectById(${pkChangeColName}));
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
        ${className} ${column.columnName}Obj = lambdaQuery().eq(${className}::get${column.capitalColumnName}, resource.get${column.capitalColumnName}()).one();
        if (ObjectUtil.isNotNull(${column.columnName}Obj) && ObjectUtil.notEqual(${pkChangeColName}, ${column.columnName}Obj.get${pkCapitalColName}())) {
            throw new EntityExistException(resource.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        baseMapper.updateById(resource);
        // 清理缓存
        redisUtils.del(CacheConstant.${cacheKey}_ID + ${pkChangeColName});
    }

    @Override
    public List<${className}DTO> list${className}s(${className}Query query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), ${className}DTO.class);
    }

    @Override
    public Page<${className}DTO> list${className}s(${className}Query query, Page<${className}> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), ${className}DTO.class);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public ${className}DTO get${className}ById(${pkColumnType} id) {
        ${className} ${changeClassName} = baseMapper.selectById(id);
        Assert.notNull(${changeClassName});
        return ConvertUtils.convert(${changeClassName}, ${className}DTO.class);
    }

    @Override
    public void export${className}(List<${className}DTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(${changeClassName} -> {
            Map<String, Object> map = MapUtil.newHashMap(${(columns?size) - 1}, true);
        <#list columns as column>
            <#if column.columnKey != 'PRI'>
                <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
                <#else>
            map.put("${column.changeColumnName}", ${changeClassName}.get${column.capitalColumnName}());
                </#if>
            </#if>
        </#list>
            list.add(map);
        });
        FileUtils.downloadExcel("${apiAlias!}", list, response);
    }
}
