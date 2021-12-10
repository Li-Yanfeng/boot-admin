package org.utility.core.model;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.utility.core.interfaces.ErrorCode;
import org.utility.exception.enums.UserErrorCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

/**
 * REST API 返回结果
 *
 * @author Li Yanfeng
 */
public class Result extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码
     */
    private static final String CODE_TAG = "code";
    /**
     * 用户提示
     */
    private static final String MSG_TAG = "msg";
    /**
     * 返回数据
     */
    private static final String DATA_TAG = "data";
    /**
     * 数据总数
     */
    private static final String TOTAL_TAG = "total";


    /**
     * 业务成功返回业务代码和提示信息
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 业务成功返回业务代码,描述和返回的参数
     */
    public static Result success(Object data) {
        ErrorCode ec = UserErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            ec = UserErrorCode.CLIENT_ERROR;
        }
        return new Result(ec, data);
    }


    /**
     * 业务异常返回业务代码,提示信息
     */
    public static Result failure() {
        return failure(UserErrorCode.CLIENT_ERROR);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static Result failure(@NotNull String msg) {
        return failure(UserErrorCode.CLIENT_ERROR.getCode(), msg);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static Result failure(String code, String msg) {
        return new Result(code, msg);
    }

    /**
     * 业务异常返回业务代码,提示信息
     */
    public static Result failure(@NotNull ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getUserTip());
    }


    private Result(String code, String msg) {
        this(code, msg, null);
    }

    private Result(ErrorCode errorCode, Object data) {
        this(errorCode.getCode(), errorCode.getUserTip(), data);
    }

    private Result(String code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        // 处理数据
        if (ObjectUtil.isNotNull(data)) {
            if (data instanceof IPage) {
                // 数据列表
                super.put(DATA_TAG, ((IPage<?>) data).getRecords());
                // 总数
                super.put(TOTAL_TAG, ((IPage<?>) data).getTotal());
            } else {
                // 数据列表
                super.put(DATA_TAG, data);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
