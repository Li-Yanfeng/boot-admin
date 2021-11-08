package org.utility.base;

import java.io.Serializable;

/**
 * 基础数据查询对象,包含通用实体字段
 *
 * @author Li Yanfeng
 */
public abstract class BaseQuery implements Serializable {

    /**
     * 当前页
     */
    protected Integer current = 1;
    /**
     * 每页显示条数
     */
    protected Integer size = 10;


    public Integer getCurrent() {
        return current;
    }

    public BaseQuery setCurrent(Integer current) {
        this.current = current;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public BaseQuery setSize(Integer size) {
        this.size = size;
        return this;
    }
}
