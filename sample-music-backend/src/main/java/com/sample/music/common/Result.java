package com.sample.music.common;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.io.Serializable;

import static com.sample.music.constant.HttpStatusCode.NOT_FOUND;
import static com.sample.music.constant.HttpStatusCode.OK;
import static com.sample.music.constant.Response.ERROR;
import static com.sample.music.constant.Response.SUCCESS;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 相关数据
     */
    private T data;

    public static <T> Result<T> success(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    public static <T> Result<T> success() {
        return new Result<>(OK, SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(OK, SUCCESS, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(NOT_FOUND, ERROR, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(NOT_FOUND, message, null);
    }
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
