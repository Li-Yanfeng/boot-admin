package com.boot.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.boot.admin.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MybatisPlus 公共字段填充
 *
 * @author Li Yanfeng
 */
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入填充
     *
     * @param metaObject /
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentTime = new Date();
        // 创建人
        this.strictInsertFill(metaObject, "createBy", String.class, getUsername());
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", Date.class, currentTime);
        // 更新人
        this.strictInsertFill(metaObject, "updateBy", String.class, getUsername());
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, currentTime);
    }

    /**
     * 更新填充
     *
     * @param metaObject /
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Date currentTime = new Date();
        // 更新人
        this.strictUpdateFill(metaObject, "updateBy", String.class, getUsername());
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, currentTime);
    }

    /**
     * 获取系统用户名称
     */
    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
