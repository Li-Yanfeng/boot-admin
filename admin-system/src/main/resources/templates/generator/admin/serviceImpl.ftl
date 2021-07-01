package ${package}.${moduleName}.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.utility.base.impl.ServiceImpl;
import ${package}.${moduleName}.mapper.${className}Mapper;
import ${package}.${moduleName}.model.${className};
import ${package}.${moduleName}.service.${className}Service;
import ${package}.${moduleName}.service.dto.${className}DTO;
import ${package}.${moduleName}.service.dto.${className}Query;
import org.utility.constant.CacheConsts;
import org.utility.util.FileUtils;
import org.utility.util.RedisUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ${apiAlias!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@CacheConfig(cacheNames = {"${changeClassName}"})
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}DTO, ${className}Query, ${className}> implements ${className}Service {

    private final RedisUtils redisUtils;

    public ${className}ServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void removeByIds(Collection<Long> ids) {
        redisUtils.delByKeys(CacheKey.${className?upper_case}_ID, ids);
        super.removeByIds(ids);
    }

    @Override
    public void updateById(${className} resource) {
        super.updateById(resource);
        redisUtils.del(CacheKey.${className?upper_case}_ID + resource.get${className}Id());
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public ${className}DTO getById(Long id) {
        return super.getById(id);
    }

    @Override
    public void download(HttpServletResponse response, List<${className}DTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (${className}DTO ${changeClassName} : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(${(columns?size) - 1}, true);
            <#list columns as column>
                <#if column.columnKey != 'PRI'>
                    <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
                    <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
                    </#if>
                </#if>
            </#list>
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
