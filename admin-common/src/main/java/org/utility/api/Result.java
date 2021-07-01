package org.utility.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.utility.exception.enums.UserErrorCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API 返回结果
 *
 * @author Li Yanfeng
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码
     */
    private String code;
    /**
     * 用户提示
     */
    private String message;
    /**
     * 返回参数
     */
    private Map<String, Object> data = new HashMap<>(5);


    /**
     * 业务成功返回业务代码和提示信息
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 业务成功返回业务代码,描述和返回的参数
     */
    public static <T> Result<T> success(T data) {
        ErrorCode ec = UserErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            ec = UserErrorCode.CLIENT_ERROR;
        }
        return new Result<>(ec, data);
    }


    /**
     * 业务异常返回业务代码,提示信息
     */
    public static <T> Result<T> failure() {
        return failure(UserErrorCode.CLIENT_ERROR);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static <T> Result<T> failure(@NotNull String message) {
        return failure(UserErrorCode.CLIENT_ERROR.getCode(), message);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static <T> Result<T> failure(String code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static <T> Result<T> failure(@NotNull ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getUserTip());
    }


    public String getCode() {
        return code;
    }

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Result setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    private Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(ErrorCode errorCode, T data) {
        this(errorCode.getCode(), errorCode.getUserTip(), data);
    }

    private Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        // 处理数据
        if (data instanceof IPage) {
            // 数据列表
            this.data.put("records", ((IPage<?>) data).getRecords());
            // 当前页
            this.data.put("current", ((IPage<?>) data).getCurrent());
            // 每页显示条数
            this.data.put("size", ((IPage<?>) data).getSize());
            // 总数
            this.data.put("total", ((IPage<?>) data).getTotal());
            // 总页数
            this.data.put("pages", ((IPage<?>) data).getPages());
        } else {
            // 数据列表
            this.data.put("records", data);
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
