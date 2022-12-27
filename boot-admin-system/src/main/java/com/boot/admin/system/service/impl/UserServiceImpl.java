package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.config.bean.FileProperties;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.EntityExistException;
import com.boot.admin.security.service.OnlineUserService;
import com.boot.admin.security.service.UserCacheClean;
import com.boot.admin.system.mapper.UserJobMapper;
import com.boot.admin.system.mapper.UserMapper;
import com.boot.admin.system.mapper.UserRoleMapper;
import com.boot.admin.system.model.User;
import com.boot.admin.system.model.UserJob;
import com.boot.admin.system.model.UserRole;
import com.boot.admin.system.service.*;
import com.boot.admin.system.service.dto.*;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"user"})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserJobMapper userJobMapper;
    private final RoleService roleService;
    private final JobService jobService;
    private final DeptService deptService;
    private final UserRoleService userRoleService;
    private final UserJobService userJobService;
    private final UserCacheClean userCacheClean;
    private final OnlineUserService onlineUserService;

    private final FileProperties fileProperties;
    private final RedisUtils redisUtils;

    public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper, UserJobMapper userJobMapper,
                           RoleService roleService, JobService jobService, DeptService deptService,
                           UserRoleService userRoleService, UserJobService userJobService,
                           UserCacheClean userCacheClean, OnlineUserService onlineUserService,
                           FileProperties fileProperties, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userJobMapper = userJobMapper;
        this.roleService = roleService;
        this.jobService = jobService;
        this.deptService = deptService;
        this.userRoleService = userRoleService;
        this.userJobService = userJobService;
        this.userCacheClean = userCacheClean;
        this.onlineUserService = onlineUserService;
        this.fileProperties = fileProperties;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(UserDTO resource) {
        if (ObjectUtil.isNotNull(getUserByUsername(resource.getUsername()))) {
            throw new EntityExistException(resource.getUsername());
        }
        if (ObjectUtil.isNotNull(getUserByEmail(resource.getEmail()))) {
            throw new EntityExistException(resource.getEmail());
        }
        if (ObjectUtil.isNotNull(getUserByPhone(resource.getPhone()))) {
            throw new EntityExistException(resource.getPhone());
        }
        User user = ConvertUtils.convert(resource, User.class);
        if (ObjectUtil.isNotNull(resource.getDept())) {
            user.setDeptId(resource.getDept().getDeptId());
        }
        baseMapper.insert(user);
        final Long userId = user.getUserId();
        if (CollUtil.isNotEmpty(resource.getRoles())) {
            List<RoleSmallDTO> roles = resource.getRoles();
            roles.forEach(role -> {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(role.getRoleId());
                userRoleMapper.insert(ur);
            });
        }
        if (CollUtil.isNotEmpty(resource.getJobs())) {
            List<JobSmallDTO> jobs = resource.getJobs();
            jobs.forEach(job -> {
                UserJob uj = new UserJob();
                uj.setUserId(userId);
                uj.setJobId(job.getJobId());
                userJobMapper.insert(uj);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeUserByIds(Collection<Long> ids) {
        List<User> users = baseMapper.selectBatchIds(ids);
        for (User user : users) {
            delCaches(user.getUserId(), user.getUsername());
        }
        userMapper.deleteBatchIds(ids);
        userRoleService.removeUserRoleByUserIds(ids);
        userJobService.removeUserJobByUserIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserById(UserDTO resource) {
        Long userId = resource.getUserId();
        UserDTO userDTO = getUserById(userId);
        User user1 = getUserByUsername(resource.getUsername());
        if (ObjectUtil.isNotNull(user1) && ObjectUtil.notEqual(userId, user1.getUserId())) {
            throw new EntityExistException(resource.getUsername());
        }
        user1 = getUserByEmail(resource.getEmail());
        if (ObjectUtil.isNotNull(user1) && ObjectUtil.notEqual(userId, user1.getUserId())) {
            throw new EntityExistException(resource.getEmail());
        }
        user1 = getUserByPhone(resource.getPhone());
        if (ObjectUtil.isNotNull(user1) && ObjectUtil.notEqual(userId, user1.getUserId())) {
            throw new EntityExistException(resource.getPhone());
        }

        User user = ConvertUtils.convert(resource, User.class);
        if (ObjectUtil.isNotNull(resource.getDept())) {
            user.setDeptId(resource.getDept().getDeptId());
        }
        // 如果用户的角色改变
        if (ObjectUtil.notEqual(resource.getRoles(), userDTO.getRoles())) {
            redisUtils.del(CacheKey.DATA_USER + userId);
            redisUtils.del(CacheKey.MENU_USER + userId);
            redisUtils.del(CacheKey.ROLE_AUTH + userId);
        }
        // 如果用户被禁用，则清除用户登录信息
        if (CommonConstant.NO.equals(resource.getEnabled())) {
            onlineUserService.kickOutForUsername(resource.getUsername());
        }

        // 更新数据
        List<RoleSmallDTO> roles = resource.getRoles();
        if (CollUtil.isNotEmpty(roles)) {
            List<Long> oldRoleIds = userRoleService.listRoleIdsByUserId(userId);
            List<Long> newRoleIds = roles.stream().map(RoleSmallDTO::getRoleId).collect(Collectors.toList());
            Collection<Long> delRoleIds = CollUtil.subtract(oldRoleIds, newRoleIds);
            Collection<Long> addRoleIds = CollUtil.subtract(newRoleIds, oldRoleIds);
            if (CollUtil.isNotEmpty(delRoleIds)) {
                userRoleService.removeUserRoleByUserIdEqAndRoleIdsIn(userId, delRoleIds);
            }
            if (CollUtil.isNotEmpty(addRoleIds)) {
                addRoleIds.forEach(roleId -> {
                    UserRole ur = new UserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    userRoleMapper.insert(ur);
                });
            }
        }
        List<JobSmallDTO> jobs = resource.getJobs();
        if (CollUtil.isNotEmpty(jobs)) {
            List<Long> oldJobIds = userJobService.listJobIdsByUserId(userId);
            List<Long> newJobIds = jobs.stream().map(JobSmallDTO::getJobId).collect(Collectors.toList());
            Collection<Long> delJobIds = CollUtil.subtract(oldJobIds, newJobIds);
            Collection<Long> addJobIds = CollUtil.subtract(newJobIds, oldJobIds);
            if (CollUtil.isNotEmpty(delJobIds)) {
                userJobService.removeUserJobByUserIdEqAndJobIdsIn(userId, delJobIds);
            }
            if (CollUtil.isNotEmpty(addJobIds)) {
                addJobIds.forEach(jobId -> {
                    UserJob uj = new UserJob();
                    uj.setUserId(userId);
                    uj.setJobId(jobId);
                    userJobMapper.insert(uj);
                });
            }
        }
        // 删除缓存
        delCaches(userId, user.getUsername());
        baseMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(String username, String encryptPassword) {
        User user = new User();
        user.setPassword(encryptPassword);
        user.setPwdResetTime(LocalDateTime.now());
        lambdaUpdate().eq(User::getUsername, username).update(user);
        // 清理缓存
        flushCache(username);
    }

    @Override
    public void updateEmail(String username, String email) {
        User user = getUserByUsername(username);
        User user1 = getUserByEmail(email);
        if (ObjectUtil.notEqual(user.getUserId(), user1.getUserId())) {
            throw new EntityExistException( email);
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        lambdaUpdate().eq(User::getUsername, username).update(updateUser);
        // 清理缓存
        flushCache(username);
    }

    @Override
    public void updateCenter(User resource) {
        User user2 = getUserByPhone(resource.getPhone());
        if (ObjectUtil.notEqual(resource.getUserId(), user2.getUserId())) {
            throw new EntityExistException(resource.getPhone());
        }
        lambdaUpdate().eq(User::getUserId, resource.getUserId()).update(resource);
        // 清理缓存
        flushCache(resource.getUsername());
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
        Long userId = SecurityUtils.getCurrentUserId();
        User user = baseMapper.selectById(userId);
        Assert.notNull(user);

        String oldPath = user.getAvatarPath();
        File uploadFile = FileUtils.upload(file, fileProperties.getPath().getAvatar());
        user.setAvatarName(uploadFile.getName());
        user.setAvatarPath(Objects.requireNonNull(uploadFile).getPath());
        userMapper.updateById(user);
        if (StrUtil.isNotBlank(oldPath)) {
            FileUtils.del(oldPath);
        }
        // 清理缓存
        flushCache(user.getUsername());

        return new HashMap<String, String>() {{
            put("avatar", uploadFile.getName());
        }};
    }

    @Override
    public List<UserDTO> listUsers(UserQuery query) {
        List<UserDTO> users = ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), UserDTO.class);
        // 获取关联数据
        users.forEach(this::getRelevantData);
        return users;
    }

    @Override
    public Page<UserDTO> listUsers(UserQuery query, Page<User> page) {
        Page<UserDTO> users = ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)),
            UserDTO.class);
        List<UserDTO> records = users.getRecords();
        // 获取关联数据
        records.forEach(this::getRelevantData);
        return users;
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public UserDTO getUserById(Long id) {
        User user = baseMapper.selectById(id);
        Assert.notNull(user);
        UserDTO userDTO = ConvertUtils.convert(user, UserDTO.class);
        // 获取关联数据
        getRelevantData(userDTO);
        return userDTO;
    }

    /**
     * 根据 username 查询
     *
     * @param username 用户名
     * @return 查询结果
     */
    @Cacheable(key = "'username:' + #p0")
    @Override
    public UserDTO loadUserByUsername(String username) {
        UserDTO userDTO = ConvertUtils.convert(getUserByUsername(username), UserDTO.class);
        if (ObjectUtil.isNotNull(userDTO)) {
            // 获取关联数据
            getRelevantData(userDTO);
        }
        return userDTO;
    }

    @Override
    public void exportUser(List<UserDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(user -> {
            Map<String, Object> map = MapUtil.newHashMap(9, true);
            map.put("用户名", user.getUsername());
            map.put("角色", user);
            map.put("部门", user.getDept().getName());
            map.put("岗位", user.getJobs().stream().map(JobSmallDTO::getName).collect(Collectors.toList()));
            map.put("邮箱", user.getEmail());
            map.put("状态", CommonConstant.YES.equals(user.getEnabled()) ? "启用" : "禁用");
            map.put("手机号码", user.getPhone());
            map.put("修改密码的时间", user.getPwdResetTime());
            map.put("创建日期", user.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel("用户", list, response);
    }

    /**
     * 根据 username 查询
     *
     * @param username 用户名
     * @return 查询结果
     */
    private User getUserByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    /**
     * 根据 email 查询
     *
     * @param email 邮箱
     * @return 查询结果
     */
    private User getUserByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).one();
    }

    /**
     * 根据 phone 查询
     *
     * @param phone 手机号
     * @return 查询结果
     */
    private User getUserByPhone(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).one();
    }

    /**
     * 获取关联数据
     */
    private void getRelevantData(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        userDTO.setRoles(roleService.listRolesByUserId(userId));
        userDTO.setJobs(jobService.listJobsByUserId(userId));
        userDTO.setDept(ConvertUtils.convert(deptService.getDeptById(userDTO.getDeptId()), DeptSmallDTO.class));
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    private void delCaches(Long id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.USER_NAME + username);
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
