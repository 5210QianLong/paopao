package com.zhao.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍用户关系表
 * @TableName user_team
 */
@TableName(value ="user_team")
@Data
public class UserTeam implements Serializable {
    /**
     * 队关系表d
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 加入队伍时间
     */
    private Date joinTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer idDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}