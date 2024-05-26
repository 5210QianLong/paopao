package com.zhao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.usercenter.common.ErrorCode;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.mapper.TeamMapper;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.model.domain.UserTeam;
import com.zhao.usercenter.model.enums.TeamStatusEnums;
import com.zhao.usercenter.service.TeamService;
import com.zhao.usercenter.service.UserTeamService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author zql
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-05-21 11:13:45
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{
    @Resource
    private UserTeamService userTeamService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        //是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = loginUser.getId();
        //校验队伍人数
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum<1 || maxNum>20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
        //队伍标题
        if(StringUtils.isBlank(team.getTeamName()) ||team.getTeamName().length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题人数不满足要求");
        }
        //描述
        if (StringUtils.isNotBlank(team.getDescription()) && team.getDescription().length()>512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"描述人数不满足要求");
        }
        //status
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnums enumsByValue = TeamStatusEnums.getEnumsByValue(status);
        if(enumsByValue == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态人数不满足要求");
        }
        //如果status是加密状态，一定要有密码，且密码<32
        String password = team.getPassword();
        if (TeamStatusEnums.SECRET.equals(enumsByValue) && (StringUtils.isBlank(password) || password.length()> 32) ) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不符合要求");
            }
        //校验超时时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"超时时间不符合要求");
        }
        //当前用户最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",team.getUserId());
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍已达上限");
        }
        //插入信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean teamSaved = this.save(team);
        Long teamId = team.getId();
        if (!teamSaved){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        //插入数据到队伍-用户关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        boolean userTeamSaved = userTeamService.save(userTeam);
        if (!userTeamSaved){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        return teamId;
    }
}




