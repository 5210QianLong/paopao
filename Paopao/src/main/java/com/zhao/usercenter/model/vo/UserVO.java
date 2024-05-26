package com.zhao.usercenter.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
@Data
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3424629404246780803L;
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户简介
     */
    private String profile;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;
    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户状态
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 用户角色 0-普通 1 -管理
     */
    private Integer userRole;
    /**
     * 星球编号
     */
    private String planetCode;
    /**
     * 标签
     */
    private String tags;
}
