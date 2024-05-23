package com.zhao.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.mapper.TeamMapper;
import com.zhao.usercenter.service.TeamService;
import org.springframework.stereotype.Service;

/**
* @author zql
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-05-21 11:13:45
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




