package com.boot.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.boot.admin.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MybatisPlus 公共字段填充
 *
 * @author Li Yanfeng
 */
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 创建人
     */
    private final String CREATE_BY = "createBy";
    /**
     * 创建人名称
     */
    private final String CREATE_BY_NAME = "createByName";
    /**
     * 创建时间
     */
    private final String CREATE_TIME = "createTime";

    /**
     * 更新人
     */
    private final String UPDATE_BY = "updateBy";
    /**
     * 更新人名称
     */
    private final String UPDATE_BY_NAME = "updateByName";
    /**
     * 更新时间
     */
    private final String UPDATE_TIME = "updateTime";

    /**
     * 插入填充
     *
     * @param metaObject /
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long createBy = getCurrentUserId();
        String createByName = getCurrentNickName();
        LocalDateTime createTime = LocalDateTime.now();

        // 创建人
        if (hasSetterAndValueIsNull(metaObject, CREATE_BY)) {
            this.strictInsertFill(metaObject, CREATE_BY, Long.class, createBy);
        }
        // 创建人名称
        if (hasSetterAndValueIsNull(metaObject, CREATE_BY_NAME)) {
            this.strictInsertFill(metaObject, CREATE_BY_NAME, String.class, createByName);
        }
        // 创建时间
        if (hasSetterAndValueIsNull(metaObject, CREATE_TIME)) {
            this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, createTime);
        }
        // 更新人
        if (hasSetterAndValueIsNull(metaObject, UPDATE_BY)) {
            this.strictInsertFill(metaObject, UPDATE_BY, Long.class, createBy);
        }
        // 更新人名称
        if (hasSetterAndValueIsNull(metaObject, UPDATE_BY_NAME)) {
            this.strictInsertFill(metaObject, UPDATE_BY_NAME, String.class, createByName);
        }
        // 更新时间
        if (hasSetterAndValueIsNull(metaObject, UPDATE_TIME)) {
            this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, createTime);
        }
    }

    /**
     * 更新填充
     *
     * @param metaObject /
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long updateBy = getCurrentUserId();
        String updateByName = getCurrentNickName();
        LocalDateTime updateTime = LocalDateTime.now();

        // 更新人
        if (hasSetterAndValueIsNull(metaObject, UPDATE_BY)) {
            this.strictUpdateFill(metaObject, UPDATE_BY, Long.class, updateBy);
        }
        // 更新人名称
        if (hasSetterAndValueIsNull(metaObject, UPDATE_BY_NAME)) {
            this.strictUpdateFill(metaObject, UPDATE_BY_NAME, String.class, updateByName);
        }
        // 更新时间
        if (hasSetterAndValueIsNull(metaObject, UPDATE_TIME)) {
            this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, updateTime);
        }
    }

    /**
     * 存在属性并且值为 null
     */
    private boolean hasSetterAndValueIsNull(MetaObject metaObject, String field) {
        return metaObject.hasSetter(field) && metaObject.getValue(field) == null;
    }

    /**
     * 获取系统用户ID
     */
    private Long getCurrentUserId() {
        try {
            return SecurityUtils.getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取系统用户昵称
     */
    private String getCurrentNickName() {
        try {
            return SecurityUtils.getCurrentNickName();
        } catch (Exception e) {
            return null;
        }
    }
}
