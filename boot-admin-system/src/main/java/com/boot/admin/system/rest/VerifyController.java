package com.boot.admin.system.rest;

import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.service.EmailService;
import com.boot.admin.system.service.VerifyService;
import com.boot.admin.util.enums.CodeBiEnum;
import com.boot.admin.util.enums.CodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：验证码管理")
@RestController
@RequestMapping(value = "/v1/codes")
@ResultWrapper
public class VerifyController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    public VerifyController(VerifyService verificationCodeService, EmailService emailService) {
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "重置邮箱，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/reset/emails")
    public void resetEmail(@RequestParam String email) {
        EmailVO emailVO = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVO, emailService.getEmailConfig());
    }

    @ApiOperation(value = "重置密码，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/reset/passes")
    public void resetPass(@RequestParam String email) {
        EmailVO emailVO = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVO, emailService.getEmailConfig());
    }

    @ApiOperation(value = "验证码验证")
    @GetMapping(value = "/validated")
    public void validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
    }
}
