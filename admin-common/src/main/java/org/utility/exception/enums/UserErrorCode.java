package org.utility.exception.enums;

import org.utility.core.interfaces.ErrorCode;

/**
 * 用户错误代码 { A 表示错误来源于用户 }
 * 1. 以下错误码的定义，需要提前与前端沟通
 * 2. 错误码按模块进行错误码规划
 * 3. 所有错误码枚举类均需要实现错误码接口类
 *
 * @author Li Yanfeng
 * @see UserErrorCode 错误来源于用户
 * @see SystemErrorCode 错误来源于当前系统
 * @see ThirdPartyServicesErrorCode 错误来源于第三方服务
 */
public enum UserErrorCode implements ErrorCode {

    SUCCESS("00000", "成功"),


    // A 表示错误来源于用户
    CLIENT_ERROR("A0001", "用户端错误"),
    USER_REGISTRATION_ERROR("A0100", "用户注册错误"),
    THE_USER_DID_NOT_AGREE_TO_THE_PRIVACY_AGREEMENT("A0101", "用户未同意隐私协议"),
    RESTRICTED_COUNTRY_OR_REGION_OF_REGISTRATION("A0102", "注册国家或地区受限"),
    USERNAME_VERIFICATION_FAILED("A0110", "用户名校验失败"),
    USERNAME_ALREADY_EXISTS("A0111", "用户名已存在"),
    USERNAME_CONTAINS_SENSITIVE_WORDS("A0112", "用户名包含敏感词"),
    USERNAME_CONTAINS_SPECIAL_CHARACTERS("A0113", "用户名包含特殊字符"),
    PASSWORD_VERIFICATION_FAILED("A0120", "密码校验失败"),
    PASSWORD_LENGTH_IS_NOT_ENOUGH("A0121", "密码长度不够"),
    THE_PASSWORD_IS_NOT_STRONG_ENOUGH("A0122", "密码强度不够"),
    CHECK_CODE_INPUT_ERROR("A0130", "校验码输入错误"),
    SMS_VERIFICATION_CODE_INPUT_ERROR("A0131", "短信校验码输入错误"),
    EMAIL_VERIFICATION_CODE_INPUT_ERROR("A0132", "邮件校验码输入错误"),
    VOICE_CHECK_CODE_INPUT_ERROR("A0133", "语音校验码输入错误"),
    USER_ID_IS_ABNORMAL("A0140", "用户证件异常"),
    USER_ID_TYPE_IS_NOT_SELECTED("A0141", "用户证件类型未选择"),
    ILLEGAL_VERIFICATION_OF_MAINLAND_ID_CARD_NUMBER("A0142", "大陆身份证编号校验非法"),
    ILLEGAL_PASSPORT_NUMBER_VERIFICATION("A0143", "护照编号校验非法"),
    MILITARY_OFFICER_ID_NUMBER_VERIFICATION_IS_ILLEGAL("A0144", "军官证编号校验非法"),
    USER_BASIC_INFORMATION_VERIFICATION_FAILED("A0150", "用户基本信息校验失败"),
    PHONE_FORMAT_VERIFICATION_FAILED("A0151", "手机格式校验失败"),
    ADDRESS_FORMAT_VERIFICATION_FAILED("A0152", "地址格式校验失败"),
    EMAIL_FORMAT_VERIFICATION_FAILED("A0153", "邮箱格式校验失败"),
    USER_LOGIN_EXCEPTION("A0200", "用户登录异常"),
    USER_ACCOUNT_DOES_NOT_EXIST("A0201", "用户账户不存在"),
    USER_ACCOUNT_IS_FROZEN("A0202", "用户账户被冻结"),
    USER_ACCOUNT_HAS_BEEN_VOIDED("A0203", "用户账户已作废"),
    USER_PASSWORD_IS_WRONG("A0210", "用户密码错误"),
    THE_NUMBER_OF_TIMES_THE_USER_ENTERED_THE_WRONG_PASSWORD_EXCEEDED_THE_LIMIT("A0211", "用户输入密码错误次数超限"),
    USER_IDENTITY_VERIFICATION_FAILED("A0220", "用户身份校验失败"),
    USER_FINGERPRINT_RECOGNITION_FAILED("A0221", "用户指纹识别失败"),
    USER_FACE_RECOGNITION_FAILED("A0222", "用户面容识别失败"),
    THE_USER_IS_NOT_AUTHORIZED_TO_LOG_IN_BY_A_THIRD_PARTY("A0223", "用户未获得第三方登录授权"),
    USER_LOGIN_HAS_EXPIRED("A0230", "用户登录已过期"),
    USER_VERIFICATION_CODE_ERROR("A0240", "用户验证码错误"),
    USER_VERIFICATION_CODE_DOES_NOT_EXIST_OR_EXPIRED("A0241", "用户验证码不存在或已过期"),
    THE_NUMBER_OF_USER_VERIFICATION_CODE_ATTEMPTS_EXCEEDS_THE_LIMIT("A0242", "用户验证码尝试次数超限"),
    ABNORMAL_ACCESS_RIGHTS("A0300", "访问权限异常"),
    UNAUTHORIZED_ACCESS("A0301", "访问未授权"),
    AUTHORIZING("A0302", "正在授权中"),
    USER_AUTHORIZATION_REQUEST_REJECTED("A0303", "用户授权申请被拒绝"),
    BLOCKED_DUE_TO_THE_PRIVACY_SETTINGS_OF_THE_VISITOR("A0310", "因访问对象隐私设置被拦截"),
    AUTHORIZATION_HAS_EXPIRED("A0311", "授权已过期"),
    NO_ACCESS_TO_API("A0312", "无权限使用 API"),
    USER_ACCESS_IS_BLOCKED("A0320", "用户访问被拦截"),
    BLACKLISTED_USERS("A0321", "黑名单用户"),
    ACCOUNT_IS_FROZEN("A0322", "账号被冻结"),
    ILLEGAL_IP_ADDRESS("A0323", "非法 IP 地址"),
    GATEWAY_ACCESS_IS_RESTRICTED("A0324", "网关访问受限"),
    REGIONAL_BLACKLIST("A0325", "地域黑名单"),
    SERVICE_ARREARS("A0330", "服务已欠费"),
    USER_SIGNATURE_IS_ABNORMAL("A0340", "用户签名异常"),
    RSA_SIGNATURE_ERROR("A0341", "RSA 签名错误"),
    USER_REQUEST_PARAMETER_ERROR("A0400", "用户请求参数错误"),
    CONTAINS_ILLEGAL_AND_MALICIOUS_REDIRECT_LINKS("A0401", "包含非法恶意跳转链接"),
    INVALID_USER_INPUT("A0402", "无效的用户输入"),
    DATA_HAS_UNIQUENESS("A0403", "数据具有唯一性"),
    REQUEST_DATA_NOT_FOUND("A0404", "请求数据未找到"),
    REQUIRED_PARAMETERS_FOR_REQUEST_ARE_EMPTY("A0410", "请求必填参数为空"),
    USER_ORDER_NUMBER_IS_EMPTY("A0411", "用户订单号为空"),
    THE_ORDER_QUANTITY_IS_EMPTY("A0412", "订购数量为空"),
    MISSING_TIMESTAMP_PARAMETER("A0413", "缺少时间戳参数"),
    ILLEGAL_TIMESTAMP_PARAMETER("A0414", "非法的时间戳参数"),
    THE_REQUEST_PARAMETER_VALUE_EXCEEDS_THE_ALLOWED_RANGE("A0420", "请求参数值超出允许的范围"),
    PARAMETER_FORMAT_DOES_NOT_MATCH("A0421", "参数格式不匹配"),
    ADDRESS_NOT_IN_SERVICE("A0422", "地址不在服务范围"),
    TIME_IS_OUT_OF_SERVICE("A0423", "时间不在服务范围"),
    AMOUNT_EXCEEDS_LIMIT("A0424", "金额超出限制"),
    THE_QUANTITY_EXCEEDS_THE_LIMIT("A0425", "数量超出限制"),
    THE_TOTAL_NUMBER_OF_REQUESTS_FOR_BATCH_PROCESSING_EXCEEDS_THE_LIMIT("A0426", "请求批量处理总个数超出限制"),
    REQUEST_JSON_PARSING_FAILED("A0427", "请求 JSON 解析失败"),
    USER_INPUT_IS_ILLEGAL("A0430", "用户输入内容非法"),
    CONTAINS_PROHIBITED_SENSITIVE_WORDS("A0431", "包含违禁敏感词"),
    PICTURE_CONTAINS_PROHIBITED_INFORMATION("A0432", "图片包含违禁信息"),
    FILE_INFRINGES_COPYRIGHT("A0433", "文件侵犯版权"),
    USER_OPERATION_IS_ABNORMAL("A0440", "用户操作异常"),
    USER_PAYMENT_TIMEOUT("A0441", "用户支付超时"),
    CONFIRM_ORDER_TIMEOUT("A0442", "确认订单超时"),
    ORDER_CLOSED("A0443", "订单已关闭"),
    USER_REQUEST_SERVICE_EXCEPTION("A0500", "用户请求服务异常"),
    THE_NUMBER_OF_REQUESTS_EXCEEDS_THE_LIMIT("A0501", "请求次数超出限制"),
    THE_NUMBER_OF_CONCURRENT_REQUESTS_EXCEEDS_THE_LIMIT("A0502", "请求并发数超出限制"),
    USER_OPERATION_PLEASE_WAIT("A0503", "用户操作请等待"),
    WEBSOCKET_CONNECTION_ABNORMAL("A0504", "WebSocket 连接异常"),
    WEBSOCKET_CONNECTION_DISCONNECTED("A0505", "WebSocket 连接断开"),
    USER_REPEATED_REQUEST("A0506", "用户重复请求"),
    ABNORMAL_USER_RESOURCES("A0600", "用户资源异常"),
    INSUFFICIENT_ACCOUNT_BALANCE("A0601", "账户余额不足"),
    INSUFFICIENT_USER_DISK_SPACE("A0602", "用户磁盘空间不足"),
    INSUFFICIENT_USER_MEMORY("A0603", "用户内存空间不足"),
    INSUFFICIENT_USER_OSS_CAPACITY("A0604", "用户 OSS 容量不足"),
    USER_QUOTA_HAS_BEEN_USED_UP("A0605", "用户配额已用光"),
    USER_UPLOAD_FILE_IS_ABNORMAL("A0700", "用户上传文件异常"),
    USER_UPLOAD_FILE_TYPE_DOES_NOT_MATCH("A0701", "用户上传文件类型不匹配"),
    USER_UPLOAD_FILE_IS_TOO_LARGE("A0702", "用户上传文件太大"),
    USER_UPLOAD_IMAGE_IS_TOO_LARGE("A0703", "用户上传图片太大"),
    USER_UPLOADED_VIDEO_IS_TOO_LARGE("A0704", "用户上传视频太大"),
    THE_COMPRESSED_FILE_UPLOADED_BY_THE_USER_IS_TOO_LARGE("A0705", "用户上传压缩文件太大"),
    THE_USER_CURRENT_VERSION_IS_ABNORMAL("A0800", "用户当前版本异常"),
    THE_USER_INSTALLED_VERSION_DOES_NOT_MATCH_THE_SYSTEM("A0801", "用户安装版本与系统不匹配"),
    USER_INSTALLED_VERSION_IS_TOO_LOW("A0802", "用户安装版本过低"),
    USER_INSTALLED_VERSION_IS_TOO_HIGH("A0803", "用户安装版本过高"),
    USER_INSTALLED_VERSION_HAS_EXPIRED("A0804", "用户安装版本已过期"),
    USER_API_REQUEST_VERSION_DOES_NOT_MATCH("A0805", "用户 API 请求版本不匹配"),
    USER_API_REQUEST_VERSION_IS_TOO_HIGH("A0806", "用户 API 请求版本过高"),
    USER_API_REQUEST_VERSION_IS_TOO_LOW("A0807", "用户 API 请求版本过低"),
    USER_PRIVACY_IS_NOT_AUTHORIZED("A0900", "用户隐私未授权"),
    USER_PRIVACY_IS_NOT_SIGNED("A0901", "用户隐私未签署"),
    USER_VIDEO_CAMERA_IS_NOT_AUTHORIZED("A0902", "用户摄像头未授权"),
    USER_CAMERA_IS_NOT_AUTHORIZED("A0903", "用户相机未授权"),
    USER_PICTURE_LIBRARY_IS_NOT_AUTHORIZED("A0904", "用户图片库未授权"),
    USER_FILE_IS_NOT_AUTHORIZED("A0905", "用户文件未授权"),
    USER_LOCATION_INFORMATION_IS_NOT_AUTHORIZED("A0906", "用户位置信息未授权"),
    USER_ADDRESS_BOOK_IS_NOT_AUTHORIZED("A0907", "用户通讯录未授权"),
    USER_EQUIPMENT_IS_ABNORMAL("A1000", "用户设备异常"),
    USER_CAMERA_IS_ABNORMAL("A1001", "用户相机异常"),
    USER_MICROPHONE_IS_ABNORMAL("A1002", "用户麦克风异常"),
    THE_USER_EARPIECE_IS_ABNORMAL("A1003", "用户听筒异常"),
    USER_SPEAKER_IS_ABNORMAL("A1004", "用户扬声器异常"),
    USER_GPS_POSITIONING_IS_ABNORMAL("A1005", "用户 GPS 定位异常");


    /**
     * 业务状态码
     */
    private final String code;
    /**
     * 用户提示
     */
    private final String userTip;

    public static UserErrorCode fromCode(String code) {
        UserErrorCode[] ecs = UserErrorCode.values();
        for (UserErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }


    /** Getter | AllArgsConstructor | toString() */
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getUserTip() {
        return userTip;
    }

    UserErrorCode(final String code, final String userTip) {
        this.code = code;
        this.userTip = userTip;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, userTip=%s} ", code, userTip);
    }
}
