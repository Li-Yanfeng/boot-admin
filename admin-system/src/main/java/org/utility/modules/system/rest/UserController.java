package org.utility.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.model.Result;
import org.utility.config.RsaProperties;
import org.utility.exception.BadRequestException;
import org.utility.modules.system.model.User;
import org.utility.modules.system.model.vo.UserPassVO;
import org.utility.modules.system.service.*;
import org.utility.modules.system.service.dto.RoleSmallDTO;
import org.utility.modules.system.service.dto.UserDTO;
import org.utility.modules.system.service.dto.UserQuery;
import org.utility.util.RsaUtils;
import org.utility.util.SecurityUtils;
import org.utility.util.enums.CodeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final DeptService deptService;
    private final DataService dataService;
    private final VerifyService verificationCodeService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, DeptService deptService,
                          DataService dataService, VerifyService verificationCodeService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.deptService = deptService;
        this.dataService = dataService;
        this.verificationCodeService = verificationCodeService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation(value = "新增用户")
    @Log(value = "新增用户")
    @PreAuthorize(value = "@authorize.check('user:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody UserDTO resource) {
        checkLevel(resource);
        // 密码加密
        if (StrUtil.isNotBlank(resource.getPassword())) {
            resource.setPassword(passwordEncoder.encode(resource.getPassword()));
        } else {
            // 默认密码 123456
            resource.setPassword(passwordEncoder.encode("123456"));
        }
        userService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除用户")
    @Log(value = "删除用户")
    @PreAuthorize(value = "@authorize.check('user:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel =
                    Collections.min(roleService.listByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            Integer optLevel =
                    Collections.min(roleService.listByUsersId(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.getById(id).getUsername());
            }
        }
        userService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改用户")
    @Log(value = "修改用户")
    @PreAuthorize(value = "@authorize.check('user:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody UserDTO resource) throws Exception {
        checkLevel(resource);
        userService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "修改密码")
    @NoRepeatSubmit
    @PostMapping(value = "/updatePass")
    public Result updatePass(@RequestBody UserPassVO passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        UserDTO user = userService.getByUsername(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }

        userService.updatePassword(user.getUsername(), passwordEncoder.encode(newPass));
        return Result.success();
    }

    @ApiOperation(value = "修改头像")
    @NoRepeatSubmit
    @PostMapping(value = "/updateAvatar")
    public Result updateAvatar(@RequestParam MultipartFile avatar) {
        return Result.success(userService.updateAvatar(avatar));
    }

    @ApiOperation(value = "修改邮箱")
    @Log(value = "修改邮箱")
    @NoRepeatSubmit
    @PostMapping(value = "/updateEmail/{code}")
    public Result updateEmail(@PathVariable String code, @RequestBody User user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPassword());
        UserDTO userDto = userService.getByUsername(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(), user.getEmail());
        return Result.success();
    }

    @ApiOperation(value = "修改用户：个人中心")
    @Log(value = "修改用户：个人中心")
    @NoRepeatSubmit
    @PutMapping(value = "/center")
    public Result center(@Validated @RequestBody User resources) {
        if (!resources.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return Result.success();
    }

    @ApiOperation(value = "查询用户")
    @PreAuthorize(value = "@authorize.check('user:list')")
    @GetMapping
    public Result page(UserQuery query) {
        if (!ObjectUtils.isEmpty(query.getDeptId())) {
            query.getDeptIds().add(query.getDeptId());
            query.getDeptIds().addAll(deptService.listDeptChildren(query.getDeptId(), deptService.listByPid(query.getDeptId())));
        }
        // 数据权限
        List<Long> dataScopes = dataService.getDeptIds(userService.getByUsername(SecurityUtils.getCurrentUsername()));
        // query.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(query.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            query.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(query.getDeptIds())) {
                return Result.success(userService.page(query));
            }
        } else {
            // 否则取并集
            query.getDeptIds().addAll(dataScopes);
            return Result.success(userService.page(query));
        }
        return Result.success(new Page());
    }

    @ApiOperation(value = "导出用户")
    @Log(value = "导出用户")
    @PreAuthorize(value = "@authorize.check('user:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, UserQuery query) throws IOException {
        userService.download(response, userService.list(query));
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(UserDTO resources) {
        Integer currentLevel =
                Collections.min(roleService.listByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel =
                roleService.getLevelByRoles(resources.getRoles().stream().map(RoleSmallDTO::getRoleId).collect(Collectors.toSet()));
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
