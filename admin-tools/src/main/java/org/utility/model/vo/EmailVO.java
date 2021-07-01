package org.utility.model.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *  发送邮件时，接收参数的类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public class EmailVO {

    /**
     * 收件人，支持多个收件人
     */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;
    /**
     * 内容
     */
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
        return "EmailVO{" +
                "tos=" + tos +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
