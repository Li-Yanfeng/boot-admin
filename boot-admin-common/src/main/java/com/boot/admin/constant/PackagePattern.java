package com.boot.admin.constant;

/**
 * 包定义
 *
 * @author Li Yanfeng
 */
public class PackagePattern {

    /**
     * base包路径
     */
    public static final String BASE_PATH = "com.boot.admin";
    /**
     * 通配符
     */
    public static final String DOT_STAR = ".**";
    /**
     * base包路径 + 通配符
     */
    public static final String BASE_PATH_STAR = BASE_PATH + DOT_STAR;
    /**
     * model包路径
     */
    public static final String MODEL_PATH_STAR = BASE_PATH_STAR + ".model";
    /**
     * mapper包路径
     */
    public static final String MAPPER_PATH_STAR = BASE_PATH_STAR + ".mapper";
    /**
     * service包路径
     */
    public static final String SERVICE_PATH_STAR = BASE_PATH_STAR + ".service";
    /**
     * controller包路径，通配符 {@value}
     */
    public static final String CONTROLLER_PATH_STAR = BASE_PATH_STAR + ".rest";
}
