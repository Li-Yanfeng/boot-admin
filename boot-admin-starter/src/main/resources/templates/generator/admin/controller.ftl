package ${package}.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import ${package}.model.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}DTO;
import ${package}.service.dto.${className}Query;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author ${author}
 * @date ${date}
 */
@Tag(name = "${apiAlias!}管理")
@RestController
@RequestMapping(value = "/${changeClassName}s")
@ResultWrapper
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;

    public ${className}Controller(${className}Service ${changeClassName}Service) {
        this.${changeClassName}Service = ${changeClassName}Service;
    }

    @Operation(summary = "新增${apiAlias!}")
    @Log(value = "新增${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody ${className} resource) {
        ${changeClassName}Service.save${className}(resource);
    }

    @Operation(summary = "删除${apiAlias!}")
    @Log(value = "删除${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<${pkColumnType}> ids) {
        ${changeClassName}Service.remove${className}ByIds(ids);
    }

    @Operation(summary = "修改${apiAlias!}")
    @Log(value = "修改${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody ${className} resource) {
        ${changeClassName}Service.update${className}ById(resource);
    }

    @Operation(summary = "查询${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:list')")
    @GetMapping
    public Page<${className}DTO> list(${className}Query query, Page<${className}> page) {
        return ${changeClassName}Service.list${className}s(query, page);
    }

    @Operation(summary = "查询单个${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:list')")
    @GetMapping(value = "/{id}")
    public ${className}DTO info(@PathVariable Long id) {
        return ${changeClassName}Service.get${className}ById(id);
    }

    @Operation(summary = "导出${apiAlias!}")
    @Log(value = "导出${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}s:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, ${className}Query query) {
        ${changeClassName}Service.export${className}(${changeClassName}Service.list${className}s(query), response);
    }
}
