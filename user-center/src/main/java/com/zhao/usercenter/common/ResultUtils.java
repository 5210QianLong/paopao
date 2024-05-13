package com.zhao.usercenter.common;

/**
 * 返回工具类
 * @author zql
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T date){
       return new BaseResponse<>(0, date, "ok");
    }

    /**
     * 错误信息返回
     * @param errorCode 错误信息枚举
     * @return 通用返回类
     */
    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static  BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }
    public static  BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }
}
