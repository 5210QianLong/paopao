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
public class userLoginRequset implements Serializable {
    @Serial
    private static final long serialVersionUID = -3389334385410071820L;
    private String userAccount;
    private String userPassword;

}
