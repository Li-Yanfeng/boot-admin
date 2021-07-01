package ${package}.${moduleName}.service;

import org.utility.base.Service;
import ${package}.${moduleName}.model.${className};
import ${package}.${moduleName}.service.dto.${className}DTO;
import ${package}.${moduleName}.service.dto.${className}Query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ${apiAlias!} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${className}Service extends Service<${className}DTO, ${className}Query, ${className}> {

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<${className}DTO> queryAll) throws IOException;
}
