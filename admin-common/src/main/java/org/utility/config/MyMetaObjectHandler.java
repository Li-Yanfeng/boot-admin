package org.utility.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.utility.util.SecurityUtils;

import java.time.LocalDateTime;

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
        // 创建人
        this.strictInsertFill(metaObject, "createBy", String.class, getUsername());
        // 更新人
        this.strictInsertFill(metaObject, "updatedBy", String.class, getUsername());
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // 是否删除
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
    }

    /**
     * 更新填充
     *
     * @param metaObject /
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新人
        this.strictUpdateFill(metaObject, "updatedBy", String.class, getUsername());
        // 更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}