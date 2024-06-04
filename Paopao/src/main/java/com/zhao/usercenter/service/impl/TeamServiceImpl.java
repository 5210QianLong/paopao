package com.zhao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.usercenter.common.BaseResponse;
import com.zhao.usercenter.common.ErrorCode;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.mapper.UserTeamMapper;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.mapper.TeamMapper;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.model.domain.UserTeam;
import com.zhao.usercenter.model.dto.TeamQuery;
import com.zhao.usercenter.model.enums.TeamStatusEnums;
import com.zhao.usercenter.model.requset.TeamJoinRequest;
import com.zhao.usercenter.model.requset.TeamUpdateRequest;
import com.zhao.usercenter.model.requset.TeamquitRequest;
import com.zhao.usercenter.model.vo.TeamUserVO;
import com.zhao.usercenter.model.vo.UserVO;
import com.zhao.usercenter.service.TeamService;
import com.zhao.usercenter.service.UserService;
import com.zhao.usercenter.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;

import static com.zhao.usercenter.common.ErrorCode.NULL_ERROR;
import static com.zhao.usercenter.common.ErrorCode.PARAMS_ERROR;
import static com.zhao.usercenter.constant.UserConstant.ADMIN_ROLE;

/**
* @author zql
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-05-21 11:13:45
*/
@Slf4j
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private  UserService userService;
    @Autowired
    private UserTeamMapper userTeamMapper;

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
        if (maxNum<1 || maxNum>10) {
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
        team.setLeaderId(userId);
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

    @Override
    public List<TeamUserVO> getUserList(TeamQuery teamQuery,Boolean isAdmin) {
        //根据列表条件查询
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        Long id = teamQuery.getId();
        if (id != null && id > 0){
            queryWrapper.eq("id",id);
        }
        //根据搜索文本区查队伍
        String searchText = teamQuery.getSearchText();
        if (StringUtils.isNotBlank(searchText)){
            queryWrapper.and(qw->qw.like("team_name",searchText).or().like("description",searchText));
        }
        String teamName1 = teamQuery.getTeamName();
        if (StringUtils.isNotBlank(teamName1)){
            queryWrapper.like("team_name", teamName1);
        }
        String description1 = teamQuery.getDescription();
        if (StringUtils.isNotBlank(description1)){
            queryWrapper.like("description", description1);
        }
        Integer maxNum1 = teamQuery.getMaxNum();
        if (maxNum1 != null && maxNum1 > 0){
            queryWrapper.eq("max_num", maxNum1);
        }
        Integer status1 = teamQuery.getStatus();
        //只有管理员才能查看所有队伍
        TeamStatusEnums enumsByValue = TeamStatusEnums.getEnumsByValue(status1);
        if (enumsByValue == null){
            enumsByValue = TeamStatusEnums.PUBLIC;
        }
        if(Boolean.TRUE.equals(!isAdmin) && !TeamStatusEnums.PUBLIC.equals(enumsByValue)){
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        queryWrapper.eq("status", enumsByValue.getValue());
        //过期队伍不展示
        //ge() 使用ge()方法筛选出列值大于等于给定值的数据
        // queryWrapper.ge("expire_time", expireTime1);
        // lt()它用于筛选出小于指定值的结果。
        //选择那些expire_time字段的值小于当前日期或expire_time字段的值为NULL的记录。
        queryWrapper.and(qw->qw.gt("expire_time",new Date()).or().isNull("expire_time"));
        Long userId1 = teamQuery.getUserId();
        if(userId1 != null && userId1 > 0){
            queryWrapper.eq("user_id", userId1);
        }
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }
        return getUserList(teamList,userService);
    }

    @Override
    public List<Team> getTeamListByUser(User loginUser) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        if (CollectionUtils.isEmpty(userTeamList)){
            throw new BusinessException(NULL_ERROR);
        }
        Long[] teamIds = new Long[userTeamList.size()];
        for (int i = 0; i < userTeamList.size(); i++){
                teamIds[i] = userTeamList.get(i).getTeamId();
        }
        //查询team
        QueryWrapper<Team> qw = new QueryWrapper<>();
        qw.in("id", teamIds);
        List<Team> teamList = this.list(qw);
        if (CollectionUtils.isEmpty(teamList)){
            throw new BusinessException(NULL_ERROR);
        }
        return teamList;
    }

    public static List<TeamUserVO> getUserList(List<Team> teamList,UserService userService) {
        //关联查询队伍创建人的信息
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        for (Team team : teamList){
            Long createTeamUserId = team.getUserId();
            if (createTeamUserId == null){
                continue;
            }
            User user = userService.getById(createTeamUserId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team,teamUserVO);//这样teamUserVO里面就有数据了
            //用户脱敏
            User safetyUser = userService.getSafetyUser(user);
            if (safetyUser != null){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(safetyUser,userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        //关联查询队伍创建人的信息
        return teamUserVOList;
    }
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id != null && id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!Objects.equals(loginUser.getId(),oldTeam.getUserId()) && userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        //如果用户传入的数据和旧值一样，直接返回
        //这里注意过期时间，
        if (teamUpdateRequest.getId().equals(oldTeam.getId())
                && teamUpdateRequest.getTeamName().equals(oldTeam.getTeamName())
                && teamUpdateRequest.getDescription().equals(oldTeam.getDescription())
                && teamUpdateRequest.getStatus().equals(oldTeam.getStatus())
                && teamUpdateRequest.getExpireTime().equals(oldTeam.getExpireTime())
                && teamUpdateRequest.getUserId().equals(oldTeam.getUserId())
        ){
            return true;
        }
        //查看status的状态
        TeamStatusEnums enumsByValue = TeamStatusEnums.getEnumsByValue(teamUpdateRequest.getStatus());
        if (enumsByValue.equals(TeamStatusEnums.SECRET) && StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

        BeanUtils.copyProperties(teamUpdateRequest,oldTeam);
        return this.updateById(oldTeam);
    }

    @Override
    public Boolean userJoinTeam(TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //用户最多加入5个队伍  （查用户表）
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //队伍必须存在，且只能加入未满和未过期的队伍
        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //(查队伍表)
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //不能加入自己创建的队伍
//        if (Objects.equals(team.getUserId(), loginUser.getId())){
//            throw new BusinessException(ErrorCode.NULL_ERROR,"不能加入重复的队伍");
//        }
        //禁止加入私有的队伍
        Integer status = team.getStatus();
        TeamStatusEnums teamStatusEnums = TeamStatusEnums.getEnumsByValue(status);
        if (TeamStatusEnums.PRIVATE.equals(teamStatusEnums)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不能加入私有的队伍");
        }
        //校验加入加密的队伍
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnums.SECRET.equals(teamStatusEnums)){
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        //校验队伍是否过期
        Date expireTime = team.getExpireTime();
        if (expireTime != null && (expireTime.before(new Date()) || expireTime.equals(new Date()))){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验队伍是否已满   (查队伍-用户关系表)
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        long teamCount = userTeamService.count(queryWrapper);
        if (teamCount <=0 || teamCount >=5){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍已满");
        }
        //到队伍-用户表里查 (查队伍-用户关系表)
        queryWrapper = new QueryWrapper<>();//复用，不用新起名字
        queryWrapper.eq("user_id", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        if (userTeamList.size() >= 5){
            throw new BusinessException(ErrorCode.NULL_ERROR,"加入队伍已达上限");
        }
        //不能加入重复的队伍
        userTeamList.forEach(userTeam -> {if (Objects.equals(userTeam.getTeamId(), teamId)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"不能加入重复的队伍");
        }});
        //新增队伍-用户关系表一条数据
        UserTeam newUserTeam = new UserTeam();
        newUserTeam.setTeamId(teamId);
        newUserTeam.setUserId(loginUser.getId());
        newUserTeam.setJoinTime(new Date());
        return userTeamService.save(newUserTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean userQuitTeam(TeamquitRequest teamquitRequest, User loginUser) {
        if (teamquitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = loginUser.getId();
        Team team = getTeamById(teamquitRequest);
        Long teamId = teamquitRequest.getTeamId();
        //当前用户是否在队伍里
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
        long count1 = userTeamService.count(queryWrapper);
        if (count1 == 0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //查询该队伍里有多少用户
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        long count = userTeamService.count(queryWrapper);
        if (count <=0 || count >5){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //队伍只剩一个人
        if (count == 1){
            log.info("队伍只剩一个人退出队伍");
            this.removeById(teamId);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("team_id", teamId);
           return userTeamService.remove(queryWrapper);
        }
        //是否是创建人
        if(!Objects.equals(team.getLeaderId(), userId)){
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            log.info("非创建人退出队伍");
            return userTeamService.remove(queryWrapper);
        }else {
            //是创建人
            log.info("创建人退出队伍");
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", team.getLeaderId());
            queryWrapper.eq("team_id", teamId);
            UserTeam leaderTeam = userTeamService.getOne(queryWrapper);
            //根据查到的队长找到顺位继承人
            UserTeam userTeam = userTeamMapper.selectByUserId(leaderTeam.getJoinTime());
            team.setLeaderId(userTeam.getUserId());
            //删除旧的队长-用户关系
            userTeamService.remove(queryWrapper);
            //更新队伍表队长
            return this.updateById(team);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean dismissingTeam(TeamquitRequest teamquitRequest, User loginUser) {
        if(teamquitRequest == null){
            throw new BusinessException(PARAMS_ERROR);
        }
        Team team = getTeamById(teamquitRequest);
        //校验当前用户是不是队长 或 管理员
        if(!loginUser.getId().equals(team.getLeaderId()) && !loginUser.getUserRole().equals(ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        //移除所有与该队伍相关的  队伍-用户关系
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id", team.getId());
        boolean removeTeamUser = userTeamService.remove(userTeamQueryWrapper);
        if (!removeTeamUser){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除队伍-用户关系失败");
        }
        return this.removeById(team.getId());
    }

    private Team getTeamById(TeamquitRequest teamquitRequest) {
        Long teamId = teamquitRequest.getTeamId();
        if (teamId == null || teamId <= 0){
            throw new BusinessException(PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(NULL_ERROR);
        }
        return team;
    }
}




