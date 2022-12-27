package com.boot.admin.rest;

import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.model.EmailConfig;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Tag(name = "工具：邮件管理")
@RestController
@RequestMapping(value = "/email_configs")
@ResultWrapper
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "修改邮件配置")
    @Log(value = "修改邮件配置")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated @RequestBody EmailConfig resource) throws Exception {
        emailService.updateEmailConfig(resource, emailService.getEmailConfig());
    }

    @Operation(summary = "获取邮件配置")
    @GetMapping
    public EmailConfig config() {
        return emailService.getEmailConfig();
    }

    @Operation(summary = "发送邮件")
    @Log(value = "发送邮件")
    @NoRepeatSubmit
    @PostMapping
    public void sendEmail(@Validated @RequestBody EmailVO emailVo) {
        EmailConfig emailConfig = emailService.getEmailConfig();
        if (emailConfig == null || emailConfig.getConfigId() == null) {
            throw new BadRequestException("请先配置，再操作");
        }
        emailService.send(emailVo, emailConfig);
    }
}
