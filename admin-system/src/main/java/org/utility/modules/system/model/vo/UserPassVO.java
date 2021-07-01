package org.utility.modules.system.model.vo;

import java.io.Serializable;

/**
 * 修改密码的 VO 类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class UserPassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    private String oldPass;
    /**
     * 新密码
     */
    private String newPass;


    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    @Override
    public String toString() {
        return "UserPassVO{" +
                "oldPass='" + oldPass + '\'' +
                ", newPass='" + newPass + '\'' +
                '}';
    }
}
