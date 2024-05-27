package com.zhao.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.model.dto.TeamQuery;
import com.zhao.usercenter.model.requset.TeamJoinRequest;
import com.zhao.usercenter.model.requset.TeamUpdateRequest;
import com.zhao.usercenter.model.vo.TeamUserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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

    /**
     * 查询队伍里的所有用户
     * @param teamQuery
     * @param isAdmin
     * @return 用户列表
     */
    List<TeamUserVO> getUserList(TeamQuery teamQuery,Boolean isAdmin);

    /**
     * 更新队伍信息
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 用户加入队伍
     * @param teamJoinRequest
     * @return
     */
    Boolean userJoinTeam(TeamJoinRequest teamJoinRequest, HttpServletRequest request);
}
