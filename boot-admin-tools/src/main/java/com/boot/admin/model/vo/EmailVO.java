package com.boot.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "发送邮件时，接收参数的类")
public class EmailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收件人，支持多个收件人")
    @NotEmpty
    private List<String> tos;

    @Schema(description = "主题")
    @NotBlank
    private String subject;

    @Schema(description = "内容")
    @NotBlank
    private String content;


    public List<String> getTos() {
        return tos;
    }

    public void setTos(List<String> tos) {
        this.tos = tos;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailVO() {
    }

    public EmailVO(@NotEmpty List<String> tos, @NotBlank String subject, @NotBlank String content) {
        this.tos = tos;
        this.subject = subject;
        this.content = content;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
