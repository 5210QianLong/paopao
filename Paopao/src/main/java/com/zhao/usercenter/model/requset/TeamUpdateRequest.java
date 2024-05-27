package com.zhao.usercenter.model.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户注册请求体
 *
 * @author zql
 */
@Data
public class TeamUpdateRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 4173122236992416464L;
    /**
     * 队伍id
     */
    private Long id;
    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍描述
     */
    private String description;
    /**
     * 过期时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
