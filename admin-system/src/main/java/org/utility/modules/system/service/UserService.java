package org.utility.modules.system.service;

import org.springframework.web.multipart.MultipartFile;
import org.utility.base.Service;
import org.utility.modules.system.model.User;
import org.utility.modules.system.service.dto.UserDTO;
import org.utility.modules.system.service.dto.UserQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 用户 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface UserService extends Service<UserDTO, UserQuery, User> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param resource 实体对象
     */
    void save(UserDTO resource);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateById(UserDTO resource);

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     */
    void updatePassword(String username, String encryptPassword);

    /**
     * 修改头像
     *
     * @param file 文件
     * @return /
     */
    Map<String, String> updateAvatar(MultipartFile file);

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
     * 根据 username 查询
     *
     * @param username 用户名
     * @return /
     */
    UserDTO getByUsername(String username);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<UserDTO> queryAll) throws IOException;
}
