package com.boot.admin.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.User;
import com.boot.admin.system.service.dto.UserDTO;
import com.boot.admin.system.service.dto.UserQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface UserService extends Service<User> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveUser(UserDTO resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeUserByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateUserById(UserDTO resource);

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 加密后的密码
     */
    void updatePassword(String username, String encryptPassword);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 用户自助修改资料
     *
     * @param resource /
     */
    void updateCenter(User resource);

    /**
     * 修改头像
     *
     * @param file 文件
     * @return 操作结果
     */
    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<UserDTO> listUsers(UserQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<UserDTO> listUsers(UserQuery query, Page<User> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    UserDTO getUserById(Long id);

    /**
     * 根据 username 查询
     *
     * @param username 用户名
     * @return 实体对象
     */
    UserDTO loadUserByUsername(String username);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportUser(List<UserDTO> exportData, HttpServletResponse response) throws IOException;
}
