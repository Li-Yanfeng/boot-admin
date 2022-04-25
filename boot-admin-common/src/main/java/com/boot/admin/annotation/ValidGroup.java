package com.boot.admin.annotation;

/**
 * 分组校验 注解
 *
 * @author Li Yanfeng
 */
public interface ValidGroup {

    /**
     * 新增
     */
    interface Create extends ValidGroup {

    }

    /**
     * 删除
     */
    interface Delete extends ValidGroup {

    }

    /**
     * 修改
     */
    interface Update extends ValidGroup {

    }

    /**
     * 查询
     */
    interface Query extends ValidGroup {

    }
}
