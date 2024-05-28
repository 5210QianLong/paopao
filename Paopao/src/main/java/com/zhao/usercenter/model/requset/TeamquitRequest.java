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
public class TeamquitRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = -7582254134075361657L;
    /**
     * 队伍id
     */
    private Long teamId;
}
