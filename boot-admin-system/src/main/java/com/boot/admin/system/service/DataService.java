package com.boot.admin.system.service;


import com.boot.admin.system.service.dto.UserDTO;

import java.util.List;

/**
 * 数据权限 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DataService {

    /**
     * 获取数据权限
     *
     * @param user /
     * @return /
     */
    List<Long> listDeptIds(UserDTO user);
}
