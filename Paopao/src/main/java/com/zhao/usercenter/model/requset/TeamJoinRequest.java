package com.zhao.usercenter.model.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册请求体
 *
 * @author zql
 */
@Data
public class TeamJoinRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 4173122236992416464L;
    /**
     * 队伍id
     */
    private Long teamId;
    /**
     * 队伍密码
     */
    private String password;
}
