package com.boot.admin.system.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.Menu;
import com.boot.admin.system.model.vo.MenuVO;
import com.boot.admin.system.service.dto.MenuDTO;
import com.boot.admin.system.service.dto.MenuQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 菜单 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface MenuService extends Service<Menu> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveMenu(Menu resource);

    /**
     * 根据 MenuDTO 批量删除
     *
     * @param resources 数据传输对象列表
     */
    void removeMenu(Collection<MenuDTO> resources);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateMenuById(Menu resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<MenuDTO> listMenus(MenuQuery query);

    /**
     * 根据 userId 条件查询
     *
     * @param userId 用户ID
     * @return 列表查询结果
     */
    List<MenuDTO> listMenusByUserId(Long userId);

    /**
     * 获取所有子节点 (包含当前节点)
     *
     * @param childrenList 子节点列表
     * @param menus        节点列表
     * @return 列表查询结果
     */
    List<MenuDTO> listMenusChildren(List<MenuDTO> childrenList, List<MenuDTO> menus);

    /**
     * 根据 ID 获取同级与上级数据
     *
     * @param resource 实体对象
     * @param results  已获取子节点列表
     * @return 列表查询结果
     */
    List<MenuDTO> listMenusSuperior(MenuDTO resource, List<MenuDTO> results);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    MenuDTO getMenuById(Long id);

    /**
     * 构建菜单树
     *
     * @param resources 数据传输对象列表
     * @return 操作结果
     */
    List<MenuDTO> buildTree(List<MenuDTO> resources);

    /**
     * 构建菜单树
     *
     * @param resources 数据传输对象列表
     * @return 操作结果
     */
    List<MenuVO> buildMenus(List<MenuDTO> resources);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportMenu(List<MenuDTO> exportData, HttpServletResponse response) throws IOException;
}
