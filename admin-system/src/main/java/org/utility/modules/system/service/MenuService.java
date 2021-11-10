package org.utility.modules.system.service;

import org.utility.core.service.Service;
import org.utility.modules.system.model.Menu;
import org.utility.modules.system.model.vo.MenuVO;
import org.utility.modules.system.service.dto.MenuDTO;
import org.utility.modules.system.service.dto.MenuQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 菜单 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface MenuService extends Service<MenuDTO, MenuQuery, Menu> {

    /**
     * 根据 pid 查询
     *
     * @param pid 上级部门ID
     * @return /
     */
    List<MenuDTO> listByPid(Long pid);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户Id
     * @return /
     */
    List<MenuDTO> listByUserId(Long userId);

    /**
     * 获取所有子节点，包含自身ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    Set<Menu> listChildMenu(List<Menu> menuList, Set<Menu> menuSet);

    /**
     * 获取待删除的菜单
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    Set<Menu> listDeleteMenu(List<Menu> menuList, Set<Menu> menuSet);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menuDto /
     * @param menus   /
     * @return /
     */
    List<MenuDTO> getSuperior(MenuDTO menuDto, List<Menu> menus);

    /**
     * 构建菜单树
     *
     * @param menuDtos 原始数据
     * @return /
     */
    List<MenuDTO> buildTree(List<MenuDTO> menuDtos);

    /**
     * 构建菜单树
     *
     * @param menuDtos /
     * @return /
     */
    List<MenuVO> buildMenu(List<MenuDTO> menuDtos);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<MenuDTO> queryAll) throws IOException;
}
