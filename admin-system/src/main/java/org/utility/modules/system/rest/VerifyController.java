package org.utility.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.model.vo.EmailVO;
import org.utility.modules.system.service.VerifyService;
import org.utility.service.EmailService;
import org.utility.util.enums.CodeBiEnum;
import org.utility.util.enums.CodeEnum;

import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：验证码管理")
@RestController
@RequestMapping(value = "/api/code")
public class VerifyController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    public VerifyController(VerifyService verificationCodeService, EmailService emailService) {
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "重置邮箱，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/resetEmail")
    public ResponseEntity<Object> resetEmail(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo, emailService.getConfig());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "重置密码，发送验证码")
    @NoRepeatSubmit
    @PostMapping(value = "/email/resetPass")
    public ResponseEntity<Object> resetPass(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo, emailService.getConfig());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "验证码验证")
    @GetMapping(value = "/validated")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
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
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
