package com.zhao.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.usercenter.model.domain.UserTeam;
import com.zhao.usercenter.mapper.UserTeamMapper;
import com.zhao.usercenter.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author zql
* @description 针对表【user_team(队伍用户关系表)】的数据库操作Service实现
* @createDate 2024-05-21 11:29:01
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




