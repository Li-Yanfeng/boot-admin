package com.boot.admin.rest;

import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.model.EmailConfig;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "工具：邮件管理")
@RestController
@RequestMapping(value = "/api/email_configs")
@ResultWrapper
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ApiOperation(value = "修改邮件配置")
    @Log(value = "修改邮件配置")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated @RequestBody EmailConfig resource) throws Exception {
        emailService.updateEmailConfig(resource, emailService.getEmailConfig());
    }

    @ApiOperation(value = "获取邮件配置")
    @GetMapping
    public EmailConfig config() {
        return emailService.getEmailConfig();
    }

    @ApiOperation(value = "发送邮件")
    @Log(value = "发送邮件")
    @NoRepeatSubmit
    @PostMapping
    public void sendEmail(@Validated @RequestBody EmailVO emailVo) {
        emailService.send(emailVo, emailService.getEmailConfig());
    }
}
