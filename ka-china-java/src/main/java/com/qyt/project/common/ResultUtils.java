package com.qyt.project.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回工具类
 *
 * @author Linskin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUtils<T> {

    private int code;
    private T data;
    private String message;

    /**
     * 成功返回
     *
     * @param data 返回数据
     * @param <T> 数据类型
     * @return ResultUtils
     */
    public static <T> ResultUtils<T> success(T data) {
        return new ResultUtils<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回
     *
     * @param data 返回数据
     * @param message 返回消息
     * @param <T> 数据类型
     * @return ResultUtils
     */
    public static <T> ResultUtils<T> success(T data, String message) {
        return new ResultUtils<>(ErrorCode.SUCCESS.getCode(), data, message);
    }

    /**
     * 失败返回
     *
     * @param code 错误响应码
     * @param message 消息
     * @return ResultUtils
     */
    public static ResultUtils<Boolean> error(int code, String message) {
        return new ResultUtils<>(code, false, message);
    }

    /**
     * 失败返回
     *
     * @param errorCode 错误码
     * @return ResultUtils
     */
    public static ResultUtils<Boolean> error(ErrorCode errorCode) {
        return new ResultUtils<>(errorCode.getCode(), false, errorCode.getMessage());
    }
}