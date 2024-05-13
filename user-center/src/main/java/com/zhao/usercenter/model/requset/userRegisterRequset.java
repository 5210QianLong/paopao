package com.zhao.usercenter.model.requset;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author zql
 */
@Data
public class userRegisterRequset implements Serializable {
    @Serial
    private static final long serialVersionUID = -3403512013676959746L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
