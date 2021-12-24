package ${package}.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import ${package}.model.${className};
import ${package}.service.dto.${className}DTO;
import ${package}.service.dto.${className}Query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * ${apiAlias!} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${className}Service extends Service<${className}> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void save${className}(${className} resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void remove${className}ByIds(Collection<${pkColumnType}> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void update${className}ById(${className} resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<${className}DTO> list${className}s(${className}Query query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<${className}DTO> list${className}s(${className}Query query, Page<${className}> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    ${className}DTO get${className}ById(${pkColumnType} id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void export${className}(List<${className}DTO> exportData, HttpServletResponse response) throws IOException;
}
