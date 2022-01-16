package com.boot.admin.system.rest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.config.RsaProperties;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.model.Result;
import com.boot.admin.core.validation.Update;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.system.model.User;
import com.boot.admin.system.model.vo.UserPassVO;
import com.boot.admin.system.service.*;
import com.boot.admin.system.service.dto.*;
import com.boot.admin.util.RsaUtils;
import com.boot.admin.util.SecurityUtils;
import com.boot.admin.util.StringUtils;
import com.boot.admin.util.enums.CodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping(value = "/api/users")
@ResultWrapper
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final DeptService deptService;
    private final DataService dataService;
    private final VerifyService verifyService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, DeptService deptService,
                          DataService dataService, VerifyService verifyService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.deptService = deptService;
        this.dataService = dataService;
        this.verifyService = verifyService;
        this.passwordEncoder = passwordEncoder;
    }


    @ApiOperation(value = "新增用户")
    @Log(value = "新增用户")
    @PreAuthorize(value = "@authorize.check('users:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody UserDTO resource) {
        checkLevel(resource);
        // 密码加密
        resource.setPassword(passwordEncoder.encode(StringUtils.isNotBlank(resource.getPassword())
            ? resource.getPassword()
            : CommonConstant.USER_DEFAULT_PASSWORD)
        );
        userService.saveUser(resource);
    }

    @ApiOperation(value = "删除用户")
    @Log(value = "删除用户")
    @PreAuthorize(value = "@authorize.check('users:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel =
                Collections.min(roleService.listRolesByUserId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            Integer optLevel =
                Collections.min(roleService.listRolesByUserId(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.getUserById(id).getUsername());
            }
        }
        userService.removeUserByIds(ids);
    }

    @ApiOperation(value = "修改用户")
    @Log(value = "修改用户")
    @PreAuthorize(value = "@authorize.check('users:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody UserDTO resource) {
        checkLevel(resource);
        userService.updateUserById(resource);
    }

    @ApiOperation(value = "修改密码")
    @NoRepeatSubmit
    @PutMapping(value = "/pass")
    public Result updatePass(@RequestBody UserPassVO passVO) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVO.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVO.getNewPass());
        UserDTO user = userService.loadUserByUsername(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePassword(user.getUsername(), passwordEncoder.encode(newPass));
        return Result.success();
    }

    @ApiOperation(value = "修改邮箱")
    @Log(value = "修改邮箱")
    @NoRepeatSubmit
    @PutMapping(value = "/email/{code}")
    public void updateEmail(@PathVariable String code, @RequestBody User user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPassword());
        UserDTO userDTO = userService.loadUserByUsername(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verifyService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDTO.getUsername(), user.getEmail());
    }

    @ApiOperation(value = "修改用户：个人中心")
    @Log(value = "修改用户：个人中心")
    @NoRepeatSubmit
    @PutMapping(value = "/center")
    public void center(@Validated @RequestBody User resources) {
        if (ObjectUtil.notEqual(resources.getUserId(), SecurityUtils.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
    }

    @ApiOperation(value = "修改头像")
    @NoRepeatSubmit
    @PutMapping(value = "/avatar")
    public void updateAvatar(@RequestParam MultipartFile avatar) {
        userService.updateAvatar(avatar);
    }

    @ApiOperation(value = "查询用户")
    @PreAuthorize(value = "@authorize.check('users:list')")
    @GetMapping
    public Page<UserDTO> list(UserQuery query, Page<User> page) {
        if (ObjectUtil.isNotEmpty(query.getDeptId())) {
            List<DeptDTO> depts = deptService.listDeptsChildren(
                deptService.listDepts(new DeptQuery(query.getDeptId())),
                CollUtil.newArrayList(deptService.getDeptById(query.getDeptId()))
            );
            query.getDeptIds().add(query.getDeptId());
            query.getDeptIds().addAll(depts.stream().map(DeptDTO::getDeptId).collect(Collectors.toList()));

        }
        // 数据权限
        List<Long> dataScopes =
            dataService.listDeptIds(userService.loadUserByUsername(SecurityUtils.getCurrentUsername()));
        // query.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(query.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            query.getDeptIds().retainAll(dataScopes);
        } else {
            // 否则取并集
            query.getDeptIds().addAll(dataScopes);
        }
        return userService.listUsers(query, page);
    }

    @ApiOperation(value = "导出用户")
    @Log(value = "导出用户")
    @PreAuthorize(value = "@authorize.check('users:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, UserQuery query) throws IOException {
        userService.exportUser(userService.listUsers(query), response);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources 实体对象
     */
    private void checkLevel(UserDTO resources) {
        Integer currentLevel =
            roleService.listRolesByUserId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).min(Integer::min).get();
        Integer optLevel =
            roleService.getLevelByRoles(resources.getRoles().stream().map(RoleSmallDTO::getRoleId).collect(Collectors.toSet()));
        if (currentLevel > optLevel) {
            throw new BadRequestException(UserErrorCode.INSUFFICIENT_ROLE_PERMISSIONS);
        }
    }
}
