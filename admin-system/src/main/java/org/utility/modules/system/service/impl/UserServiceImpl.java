package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.utility.base.impl.ServiceImpl;
import org.utility.config.FileProperties;
import org.utility.constant.CacheConsts;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityExistException;
import org.utility.modules.security.service.OnlineUserService;
import org.utility.modules.security.service.UserCacheClean;
import org.utility.modules.system.mapper.UserJobMapper;
import org.utility.modules.system.mapper.UserMapper;
import org.utility.modules.system.mapper.UserRoleMapper;
import org.utility.modules.system.model.User;
import org.utility.modules.system.model.UserJob;
import org.utility.modules.system.model.UserRole;
import org.utility.modules.system.service.DeptService;
import org.utility.modules.system.service.UserJobService;
import org.utility.modules.system.service.UserRoleService;
import org.utility.modules.system.service.UserService;
import org.utility.modules.system.service.dto.*;
import org.utility.util.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"user"})
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDTO, UserQuery, User> implements UserService {

    private final UserMapper userMapper;
    private final DeptService deptService;
    private final UserRoleService userRoleService;
    private final UserJobService userJobService;
    private final UserRoleMapper userRoleMapper;
    private final UserJobMapper userJobMapper;
    private final UserCacheClean userCacheClean;
    private final OnlineUserService onlineUserService;

    private final FileProperties fileProperties;
    private final RedisUtils redisUtils;

    public UserServiceImpl(UserMapper userMapper, DeptService deptService, UserRoleService userRoleService,
                           UserJobService userJobService, UserRoleMapper userRoleMapper, UserJobMapper userJobMapper,
                           UserCacheClean userCacheClean, OnlineUserService onlineUserService,
                           FileProperties fileProperties, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.deptService = deptService;
        this.userRoleService = userRoleService;
        this.userJobService = userJobService;
        this.userRoleMapper = userRoleMapper;
        this.userJobMapper = userJobMapper;
        this.userCacheClean = userCacheClean;
        this.onlineUserService = onlineUserService;
        this.fileProperties = fileProperties;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(UserDTO resource) {
        UserDTO userDto = this.getByUsername(resource.getUsername());
        if (userDto != null) {
            throw new EntityExistException(User.class, "username", userDto.getUsername());
        }
        if (this.getByEmail(resource.getEmail()) != null) {
            throw new EntityExistException(User.class, "email", resource.getEmail());
        }
        if (this.getByPhone(resource.getPhone()) != null) {
            throw new EntityExistException(User.class, "phone", resource.getPhone());
        }

        User user = ConvertUtils.convert(resource, User.class);
        if (resource.getDept() != null) {
            user.setDeptId(resource.getDept().getDeptId());
        }
        userMapper.insert(user);
        final Long userId = user.getUserId();
        if (CollectionUtils.isNotEmpty(resource.getRoles())) {
            resource.getRoles().forEach(role -> {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(role.getRoleId());
                userRoleMapper.insert(ur);
            });
        }
        if (CollectionUtils.isNotEmpty(resource.getJobs())) {
            resource.getJobs().forEach(job -> {
                UserJob uj = new UserJob();
                uj.setUserId(userId);
                uj.setJobId(job.getJobId());
                userJobMapper.insert(uj);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        for (Long id : ids) {
            UserDTO user = this.getById(id);
            this.delCaches(user.getUserId(), user.getUsername());

        }
        userMapper.deleteBatchIds(ids);
        userRoleService.removeByUserIds(ids);
        userJobService.removeByUserIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(UserDTO resource) {
        UserDTO user = this.getById(resource.getUserId());
        UserDTO user1 = this.getByUsername(user.getUsername());
        User user2 = this.getByEmail(user.getEmail());
        User user3 = this.getByPhone(user.getPhone());
        if (user1 != null && ObjectUtil.notEqual(user.getUserId(), user1.getUserId())) {
            throw new EntityExistException(User.class, "username", user.getUsername());
        }
        if (user2 != null && ObjectUtil.notEqual(user.getUserId(), user2.getUserId())) {
            throw new EntityExistException(User.class, "email", user.getEmail());
        }
        if (user3 != null && ObjectUtil.notEqual(user.getUserId(), user3.getUserId())) {
            throw new EntityExistException(User.class, "phone", user.getPhone());
        }

        // 如果用户的角色改变
        if (!resource.getRoles().equals(user.getRoles())) {
            redisUtils.del(CacheConsts.DATA_USER + resource.getUserId());
            redisUtils.del(CacheConsts.MENU_USER + resource.getUserId());
            redisUtils.del(CacheConsts.ROLE_AUTH + resource.getUserId());
        }
        // 如果用户名称修改
        if (ObjectUtil.notEqual(resource.getUsername(), user.getUsername())) {
            throw new BadRequestException("不能修改用户名");
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resource.getEnabled()) {
            onlineUserService.kickOutForUsername(resource.getUsername());
        }
        if (CollUtil.isNotEmpty(resource.getRoles())) {
            userRoleService.removeByUserIds(CollUtil.newHashSet(resource.getUserId()));
            resource.getRoles().forEach(role -> {
                UserRole ur = new UserRole();
                ur.setUserId(resource.getUserId());
                ur.setRoleId(role.getRoleId());
                userRoleMapper.insert(ur);
            });
        }

        if (CollectionUtils.isNotEmpty(resource.getJobs())) {
            userJobService.removeByUserIds(CollUtil.newHashSet(resource.getUserId()));
            resource.getJobs().forEach(job -> {
                UserJob uj = new UserJob();
                uj.setUserId(resource.getUserId());
                uj.setJobId(job.getJobId());
                userJobMapper.insert(uj);
            });
        }

        user.setUsername(resource.getUsername());
        user.setEmail(resource.getEmail());
        user.setEnabled(resource.getEnabled());
        if (resource.getDept() != null) {
            user.setDeptId(resource.getDept().getDeptId());
        } else {
            user.setDeptId(null);
        }
        user.setPhone(resource.getPhone());
        user.setNickName(resource.getNickName());
        user.setGender(resource.getGender());

        this.delCaches(user.getUserId(), user.getUsername());
        userMapper.updateById(ConvertUtils.convert(user, User.class));
    }

    @Override
    public void updatePassword(String username, String encryptPassword) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getUsername, username);
        User user = new User();
        user.setPassword(encryptPassword);
        user.setPwdResetTime(new Date());
        userMapper.update(user, wrapper);
        // 清理缓存
        redisUtils.del(CacheConsts.USER_NAME + username);
        this.flushCache(username);
    }

    @Override
    public Map<String, String> updateAvatar(MultipartFile file) {
        // 文件大小验证
        FileUtils.checkSize(fileProperties.getAvatarMaxSize(), file.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtils.getExtensionName(file.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        UserDTO user = this.getByUsername(SecurityUtils.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File uploadFile = FileUtils.upload(file, fileProperties.getPath().getAvatar());
        user.setAvatarName(uploadFile.getName());
        user.setAvatarPath(Objects.requireNonNull(uploadFile).getPath());
        userMapper.updateById(ConvertUtils.convert(user, User.class));
        if (StrUtil.isNotBlank(oldPath)) {
            FileUtils.del(oldPath);
        }
        // 清理缓存
        redisUtils.del(CacheConsts.USER_NAME + user.getUsername());
        this.flushCache(user.getUsername());

        return new HashMap<String, String>() {
            {
                put("avatar", uploadFile.getName());
            }
        };
    }

    @Override
    public void updateEmail(String username, String email) {
        UserDTO user = this.getByUsername(username);
        User user1 = this.getByEmail(email);
        if (ObjectUtil.notEqual(user.getUserId(), user1.getUserId())) {
            throw new EntityExistException(User.class, "email", email);
        }
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getUsername, username);
        User userUpdate = new User();
        userUpdate.setEmail(email);
        userMapper.update(userUpdate, wrapper);

        // 清理缓存
        redisUtils.del(CacheConsts.USER_NAME + user.getUsername());
        this.flushCache(user.getUsername());
    }

    @Override
    public void updateCenter(User resource) {
        User user = this.getByPhone(resource.getPhone());
        if (ObjectUtil.notEqual(resource.getUserId(), user.getUserId())) {
            throw new EntityExistException(User.class, "phone", resource.getPhone());
        }
        UpdateWrapper<User> updater = new UpdateWrapper<>();
        updater.lambda().eq(User::getUserId, resource.getUserId());
        User userUpdate = new User();
        userUpdate.setPhone(resource.getPhone());
        userUpdate.setGender(resource.getGender());
        userUpdate.setNickName(resource.getNickName());
        userMapper.update(userUpdate, updater);
        // 清理缓存
        this.delCaches(user.getUserId(), user.getUsername());
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public UserDTO getById(Long id) {
        return super.getById(id);
    }


    @Override
    public IPage<UserDTO> page(UserQuery query) {
        Page<User> result = userMapper.selectPage(QueryHelp.page(query), QueryHelp.queryWrapper(query));
        IPage<UserDTO> page = ConvertUtils.convertPage(result, UserDTO.class);

        List<UserDTO> userDtos = page.getRecords();
        if (page.getTotal() > 0) {
            Map<Long, DeptDTO> deptMap =
                    deptService.list(new DeptQuery()).parallelStream().collect(Collectors.toMap(DeptDTO::getDeptId, Function.identity(), (x, y) -> x));

            LambdaQueryWrapper<UserRole> userRoleWrapper = Wrappers.lambdaQuery();
            userRoleWrapper.in(UserRole::getUserId, userDtos.stream().map(UserDTO::getUserId).collect(Collectors.toSet()));
            Map<Long, Set<UserRole>> usersRolesMap = userRoleMapper.selectList(userRoleWrapper).stream().collect(Collectors.groupingBy(UserRole::getUserId, Collectors.toSet()));

            LambdaQueryWrapper<UserJob> userJobWrapper = Wrappers.lambdaQuery();
            userJobWrapper.in(UserJob::getUserId, userDtos.stream().map(UserDTO::getUserId).collect(Collectors.toList()));
            Map<Long, List<UserJob>> usersJobsMap = userJobMapper.selectList(userJobWrapper).stream().collect(Collectors.groupingBy(UserJob::getUserId));

            userDtos.forEach(user -> {
                user.setDept(ConvertUtils.convert(deptMap.get(user.getDeptId()), DeptSmallDTO.class));
                if (usersRolesMap.containsKey(user.getUserId())) {
                    user.setRoles(usersRolesMap.get(user.getUserId()).stream().map(ur -> {
                        RoleSmallDTO role = new RoleSmallDTO();
                        role.setRoleId(ur.getRoleId());
                        return role;
                    }).collect(Collectors.toSet()));
                }
                if (usersJobsMap.containsKey(user.getUserId())) {
                    user.setJobs(usersJobsMap.get(user.getUserId()).stream().map(uj -> {
                        JobSmallDTO job = new JobSmallDTO();
                        job.setJobId(uj.getJobId());
                        return job;
                    }).collect(Collectors.toSet()));
                }
            });
        }
        return page;
    }


    @Override
    public UserDTO getByUsername(String username) {
        UserDTO userDto = ConvertUtils.convert(getByUsername(username), UserDTO.class);
///        userDto.setRoles();
///        userDto.setJobs();
///        userDto.setDept();
        return userDto;
    }

    @Override
    public void download(HttpServletResponse response, List<UserDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (UserDTO user : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(9, true);
            map.put("用户名", user.getUsername());
            map.put("角色", user);
            map.put("部门", user.getDept().getName());
            map.put("岗位", user.getJobs().stream().map(JobSmallDTO::getName).collect(Collectors.toList()));
            map.put("邮箱", user.getEmail());
            map.put("状态", user.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", user.getPhone());
            map.put("修改密码的时间", user.getPwdResetTime());
            map.put("创建日期", user.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }


    /**
     * 根据 email 查询
     *
     * @param email 邮箱
     * @return
     */
    private User getByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail, email);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 根据 phone 查询
     *
     * @param phone 手机号
     * @return
     */
    private User getByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        redisUtils.del(CacheConsts.USER_ID + id);
        redisUtils.del(CacheConsts.USER_NAME + username);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }
}
