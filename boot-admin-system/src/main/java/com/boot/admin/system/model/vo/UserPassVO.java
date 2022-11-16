package com.boot.admin.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "修改密码 视图展示对象")
public class UserPassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "旧密码")
    private String oldPass;

    @Schema(description = "新密码")
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
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
