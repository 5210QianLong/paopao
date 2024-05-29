package com.zhao.usercenter.common;
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据错误",""),
    NOT_LOGIN(40100,"用户未登录",""),
    NOT_AUTH(40101,"无权限",""),
    FORBIDDEN(40301,"无访问/修改权限",""),
    SYSTEM_ERROR(50000,"系统错误","");
    /**
     * code 错误码
     */
    private final int code;
    /**
     * message 错误信息
     */
    private final String message;
    /**
     * description 错误描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
