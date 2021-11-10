package org.utility.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.model.Result;
import org.utility.model.EmailConfig;
import org.utility.model.vo.EmailVO;
import org.utility.service.EmailService;

/**
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Api(tags = "工具：邮件管理")
@RestController
@RequestMapping(value = "/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ApiOperation(value = "获取邮件配置")
    @GetMapping
    public Result config() {
        return Result.success(emailService.getConfig());
    }

    @ApiOperation(value = "配置邮件")
    @Log(value = "配置邮件")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody EmailConfig emailConfig) throws Exception {
        emailService.config(emailConfig, emailService.getConfig());
        return Result.success();
    }

    @ApiOperation(value = "发送邮件")
    @Log(value = "发送邮件")
    @NoRepeatSubmit
    @PostMapping
    public Result sendEmail(@Validated @RequestBody EmailVO emailVo) {
        emailService.send(emailVo, emailService.getConfig());
        return Result.success();
    }
}
