package com.zhao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.zhao.usercenter.common.BaseResponse;
import com.zhao.usercenter.common.ErrorCode;
import com.zhao.usercenter.common.ResultUtils;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.model.dto.TeamQuery;
import com.zhao.usercenter.model.requset.TeamAddRequest;
import com.zhao.usercenter.model.requset.TeamJoinRequest;
import com.zhao.usercenter.model.requset.TeamUpdateRequest;
import com.zhao.usercenter.model.requset.TeamquitRequest;
import com.zhao.usercenter.model.vo.TeamUserVO;
import com.zhao.usercenter.service.TeamService;
import com.zhao.usercenter.service.UserService;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 队伍请求接口
 * RequestMapping("/team") 是接口路径
 * swagger开发文档 knife4j增强 <a href="http://localhost:8080/api/doc.html"/> 查看文档
 * <p>
 * 注解@Profile("dev") 防止接口消息暴露，dev 默认配置，prod 加载上线配置yml
 * @author zql
 */
@RestController
@RequestMapping("/team")
@ApiSupport(author = "zql")
@Profile("dev")
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:5173/"})
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;
    @Resource
    private UserService userService;
    @Qualifier("redisTemplate")
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        //请求参数是否为空
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean save = teamService.removeById(team.getId());
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return ResultUtils.success(true);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);
        return ResultUtils.success(result);
    }
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(@RequestParam("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }
    @GetMapping("/getTeam")
    public BaseResponse<List<Team>> getTeamListsById(@RequestParam("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Team> queryWrapper = new QueryWrapper();
        queryWrapper.eq("leader_id", id);
        List<Team> list = teamService.list(queryWrapper);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(list);
    }
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> getTeamLists(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.getUserList(teamQuery,isAdmin);
        if (teamList == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamList);
    }

    /**
     *针对当前用户已加入的队伍
     * @param request
     * @return
     */
    @GetMapping("/currentTeams")
    public BaseResponse<List<Team>> getTeamLists(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<Team> teamList = teamService.getTeamListByUser(loginUser);
        if (teamList == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamList);
    }
    @GetMapping("/recommend/page")
    public BaseResponse<Page<Team>> getRecommendTeamPage(long pageSize , long pageNum,HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String redisTeamKey = String.format("Paopao:usercenter:team:reccommend:%s",loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //如果有缓存，直接走缓存
        Page<Team> teamPage = (Page<Team>)valueOperations.get(redisTeamKey);
        if (teamPage != null) {
            return ResultUtils.success(teamPage);
        }
        //如果没有,走常规
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("user_id", loginUser.getId());
        Page<Team> page = teamService.page(new Page<>(pageNum, pageSize), queryWrapper);
        if (page==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        try {
            valueOperations.set(redisTeamKey, page,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return ResultUtils.success(page);
    }
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamList = teamService.page(page,queryWrapper);
        if (teamList == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamList);
    }
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = teamService.userJoinTeam(teamJoinRequest,request);
        return ResultUtils.success(result);
    }
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamquitRequest teamquitRequest, HttpServletRequest request){
        if (teamquitRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean result = teamService.userQuitTeam(teamquitRequest,loginUser);
        return ResultUtils.success(result);
    }
    /**
     * 队长解散队伍
     */
    @PostMapping("/dismissing")
    public BaseResponse<Boolean> dismissTeam(@RequestBody TeamquitRequest teamquitRequest, HttpServletRequest request){
        if (teamquitRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean result =  teamService.dismissingTeam(teamquitRequest,loginUser);
        return ResultUtils.success(result);
    }
}

