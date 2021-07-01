package org.utility.exception.enums;

import org.utility.api.ErrorCode;

/**
 * 系统错误代码 { B 表示错误来源于当前系统 }
 * 1. 以下错误码的定义，需要提前与前端沟通
 * 2. 错误码按模块进行错误码规划
 * 3. 所有错误码枚举类均需要实现错误码接口类
 *
 * @author Li Yanfeng
 * @see UserErrorCode 错误来源于用户
 * @see SystemErrorCode 错误来源于当前系统
 * @see ThirdPartyServicesErrorCode 错误来源于第三方服务
 */
public enum SystemErrorCode implements ErrorCode {

    // B 表示错误来源于当前系统
    SYSTEM_EXECUTION_ERROR("B0001", "系统执行出错"),
    SYSTEM_EXECUTION_TIMEOUT("B0100", "系统执行超时"),
    SYSTEM_ORDER_PROCESSING_TIMEOUT("B0101", "系统订单处理超时"),
    THE_SYSTEM_DISASTER_TOLERANCE_FUNCTION_IS_TRIGGERED("B0200", "系统容灾功能被触发"),
    SYSTEM_CURRENT_LIMIT("B0210", "系统限流"),
    SYSTEM_FUNCTION_DEGRADATION("B0220", "系统功能降级"),
    ABNORMAL_SYSTEM_RESOURCES("B0300", "系统资源异常"),
    SYSTEM_RESOURCES_ARE_EXHAUSTED("B0310", "系统资源耗尽"),
    SYSTEM_RUNNING_OUT_OF_DISK_SPACE("B0311", "系统磁盘空间耗尽"),
    SYSTEM_MEMORY_IS_EXHAUSTED("B0312", "系统内存耗尽"),
    FILE_HANDLE_EXHAUSTED("B0313", "文件句柄耗尽"),
    SYSTEM_CONNECTION_POOL_IS_EXHAUSTED("B0314", "系统连接池耗尽"),
    SYSTEM_THREAD_POOL_EXHAUSTED("B0315", "系统线程池耗尽"),
    ABNORMAL_ACCESS_TO_SYSTEM_RESOURCES("B0320", "系统资源访问异常"),
    THE_SYSTEM_FAILED_TO_READ_THE_DISK_FILE("B0321", "系统读取磁盘文件失败");


    /**
     * 业务状态码
     */
    private final String code;
    /**
     * 用户提示
     */
    private final String userTip;


    /** Getter | AllArgsConstructor | toString() */
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getUserTip() {
        return userTip;
    }

    SystemErrorCode(final String code, final String userTip) {
        this.code = code;
        this.userTip = userTip;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, userTip=%s} ", code, userTip);
    }
}
