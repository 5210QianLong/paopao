package com.zhao.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.usercenter.model.domain.UserTeam;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
* @author zql
* @description 针对表【user_team(队伍用户关系表)】的数据库操作Mapper
* @createDate 2024-05-21 11:29:01
* @Entity generator.domain.UserTeam
*/
public interface UserTeamMapper extends BaseMapper<UserTeam> {

    @Select("select * from user_team where join_time > #{leaderDate} limit 1")
    UserTeam selectByUserId(Date leaderDate);
}




