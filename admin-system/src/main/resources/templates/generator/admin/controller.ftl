package ${package}.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import ${package}.model.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}DTO;
import ${package}.service.dto.${className}Query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "${apiAlias!}管理")
@RestController
@RequestMapping(value = "/api/${changeClassName}")
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;

    public ${className}Controller(${className}Service ${changeClassName}Service) {
        this.${changeClassName}Service = ${changeClassName}Service;
    }

    @ApiOperation(value = "新增${apiAlias!}")
    @Log(value = "新增${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody ${className} resource) {
        ${changeClassName}Service.save(resource);
    }

    @ApiOperation(value = "删除${apiAlias!}")
    @Log(value = "删除${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<${pkColumnType}> ids) {
        ${changeClassName}Service.removeByIds(ids);
    }

    @ApiOperation(value = "修改${apiAlias!}")
    @Log(value = "修改${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated @RequestBody ${className} resource) {
        ${changeClassName}Service.updateById(resource);
    }

    @ApiOperation(value = "查询${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}:list')")
    @GetMapping
    public IPage<${className}DTO> page(${className}Query query) {
        return ${changeClassName}Service.page(query);
    }

    @ApiOperation(value = "导出${apiAlias!}")
    @Log(value = "导出${apiAlias!}")
    @PreAuthorize(value = "@authorize.check('${changeClassName}:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, ${className}Query query) throws IOException {
        ${changeClassName}Service.download(response, ${changeClassName}Service.list(query));
    }
}
