package com.boot.admin.security.config.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 登录验证码配置信息
 *
 * @author Li Yanfeng
 */
public class LoginCode {

    /**
     * 是否启用验证码
     */
    private Boolean enabled;
    /**
     * 验证码配置
     */
    private LoginCodeEnum codeType;
    /**
     * 验证码有效期 分钟
     */
    private Long expiration;
    /**
     * 验证码宽度
     */
    private Integer width;
    /**
     * 验证码高度
     */
    private Integer height;
    /**
     * 验证码内容长度
     */
    private Integer length;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private Integer fontSize;


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LoginCodeEnum getCodeType() {
        return codeType;
    }

    public void setCodeType(LoginCodeEnum codeType) {
        this.codeType = codeType;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }
}
