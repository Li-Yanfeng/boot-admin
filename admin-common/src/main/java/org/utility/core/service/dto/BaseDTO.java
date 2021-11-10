package org.utility.core.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * 基础数据传输对象,包含通用实体字段
 * <p>
 * 浅析VO、DTO、DO、PO的概念、区别和用处
 * https://www.cnblogs.com/qixuejia/p/4390086.html
 * </p>
 *
 * @author Li Yanfeng
 */
public abstract class BaseDTO implements Serializable {

    /**
     * 创建人
     */
    protected String createBy;
    /**
     * 更新人
     */
    protected String updateBy;
    /**
     * 创建时间
     */
    protected Timestamp createTime;
    /**
     * 更新时间
     */
    protected Timestamp updateTime;


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
