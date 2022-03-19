package com.boot.admin.config.bean;

import com.boot.admin.constant.OsConstant;

/**
 * 文件属性配置
 *
 * @author Li Yanfeng
 */
public class FileProperties {

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

    private AdminPath mac;

    private AdminPath linux;

    private AdminPath windows;


    public AdminPath getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(OsConstant.WIN)) {
            return windows;
        } else if (os.toLowerCase().startsWith(OsConstant.MAC)) {
            return mac;
        }
        return linux;
    }


    public static class AdminPath {

        private String path;

        private String avatar;


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

    public AdminPath getMac() {
        return mac;
    }

    public void setMac(AdminPath mac) {
        this.mac = mac;
    }

    public AdminPath getLinux() {
        return linux;
    }

    public void setLinux(AdminPath linux) {
        this.linux = linux;
    }

    public AdminPath getWindows() {
        return windows;
    }

    public void setWindows(AdminPath windows) {
        this.windows = windows;
    }
}

