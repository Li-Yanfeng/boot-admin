package org.utility.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.utility.util.SecurityUtils;

import java.sql.Timestamp;

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
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        // 创建人
        this.strictInsertFill(metaObject, "createBy", String.class, getUsername());
        // 更新人
        this.strictInsertFill(metaObject, "updateBy", String.class, getUsername());
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", Timestamp.class, currentTime);
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime",  Timestamp.class, currentTime);
    }

    /**
     * 更新填充
     *
     * @param metaObject /
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        // 更新人
        this.strictUpdateFill(metaObject, "updateBy", String.class, getUsername());
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime",  Timestamp.class, currentTime);
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}