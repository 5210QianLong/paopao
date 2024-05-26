package com.zhao.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.model.domain.User;

/**
* @author zql
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-21 11:13:45
*/
public interface TeamService extends IService<Team> {
    /**
     *  添加队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
