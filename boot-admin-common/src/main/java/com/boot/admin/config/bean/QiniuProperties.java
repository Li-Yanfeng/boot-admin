package com.boot.admin.config.bean;

/**
 * 七牛云属性配置
 *
 * @author Li Yanfeng
 */
public class QiniuProperties {

    /**
     * 文件大小限制
     */
    private Long maxSize;
    /**
     * 头像大小限制
     */
    private Long avatarMaxSize;
    /**
     * 压缩图片
     */
    private boolean compressImage;

    /**
     * 文件路径
     */
    private String path;
    /**
     * 头像路径
     */
    private String avatar;


    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getAvatarMaxSize() {
        return avatarMaxSize;
    }

    public void setAvatarMaxSize(Long avatarMaxSize) {
        this.avatarMaxSize = avatarMaxSize;
    }

    public boolean isCompressImage() {
        return compressImage;
    }

    public void setCompressImage(boolean compressImage) {
        this.compressImage = compressImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

