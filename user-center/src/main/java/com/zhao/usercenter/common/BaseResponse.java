package com.zhao.usercenter.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 * @param <T> 泛型数据接口
 * @author zql
 */
@Data
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -7517637134919381760L;
    private int code;
    private T date;
    private String message;
    private String description;

    public BaseResponse(int code, T date, String message,String description) {
        this.code = code;
        this.date = date;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code, T date, String message) {
        this.code = code;
        this.date = date;
        this.message = message;
        this.description = "";
    }
    public BaseResponse(int code, T date) {
        this.code = code;
        this.date = date;
        this.message="";
        this.description = "";
    }
    public BaseResponse(ErrorCode errorCode) {
       this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
