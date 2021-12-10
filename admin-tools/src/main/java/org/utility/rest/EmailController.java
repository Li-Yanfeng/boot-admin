package org.utility.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.model.EmailConfig;
import org.utility.model.vo.EmailVO;
import org.utility.service.EmailService;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "邮件配置管理")
@RestController
@RequestMapping(value = "/v1/email_configs")
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
