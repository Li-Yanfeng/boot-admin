package com.boot.admin.system.rest;

import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.service.EmailService;
import com.boot.admin.system.service.VerifyService;
import com.boot.admin.util.enums.CodeBiEnum;
import com.boot.admin.util.enums.CodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "系统：验证码管理")
@RestController
@RequestMapping(value = "/codes")
@ResultWrapper
public class VerifyController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    public VerifyController(VerifyService verificationCodeService, EmailService emailService) {
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
    }

    @Operation(summary = "重置邮箱，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/reset/emails")
    public void resetEmail(@RequestParam String email) {
        EmailVO emailVO = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVO, emailService.getEmailConfig());
    }

    @Operation(summary = "重置密码，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/reset/passes")
    public void resetPass(@RequestParam String email) {
        EmailVO emailVO = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVO, emailService.getEmailConfig());
    }

    @Operation(summary = "验证码验证")
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
