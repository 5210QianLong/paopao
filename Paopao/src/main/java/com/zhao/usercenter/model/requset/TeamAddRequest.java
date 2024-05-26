package com.zhao.usercenter.model.requset;

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
public class TeamAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 250770966443951684L;
    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-公开 1-私有 2-加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;
}
