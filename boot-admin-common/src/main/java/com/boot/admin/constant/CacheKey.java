package com.boot.admin.constant;

/**
 * 关于缓存的 Key 集合
 *
 * @author Li Yanfeng
 */
public class CacheKey {

    /**
     * 菜单
     */
    public static final String MENU_ID = "menu::id:";
    public static final String MENU_PID = "menu::pid:";
    public static final String MENU_USER = "menu::user:";
    /**
     * 角色授权
     */
    public static final String ROLE_AUTH = "role::auth:";
    /**
     * 角色信息
     */
    public static final String ROLE_ID = "role::id:";
    /**
     * 用户
     */
    public static final String USER_ID = "user::id:";
    public static final String USER_NAME = "user::username:";
    /**
     * 数据
     */
    public static final String DATA_USER = "data::user:";

    /**
     * 部门
     */
    public static final String DEPT_ID = "dept::id:";
    public static final String DEPT_PID = "dept::pid:";
    /**
     * 岗位
     */
    public static final String JOB_ID = "job::id:";
    /**
     * 数据字典
     */
    public static final String DICT_ID = "dict::id:";
    public static final String DICT_NAME = "dict::name:";
    public static final String DICT_DETAIL_DICT_ID = "dictDetail::dictId:";


    /**
     * 阿里支付配置
     */
    public static final String ALIPAY_CONFIG = "alipay::config";
    /**
     * 邮箱配置
     */
    public static final String EMAIL_CONFIG = "email::config";
    /**
     * 七牛云配置
     */
    public static final String QINIU_CONFIG = "qiniu::config";
}
