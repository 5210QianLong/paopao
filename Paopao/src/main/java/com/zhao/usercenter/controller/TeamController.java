package com.zhao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.zhao.usercenter.common.BaseResponse;
import com.zhao.usercenter.common.ErrorCode;
import com.zhao.usercenter.common.ResultUtils;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.model.domain.Team;
import com.zhao.usercenter.model.dto.TeamQuery;
import com.zhao.usercenter.service.TeamService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean save = teamService.save(team);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return ResultUtils.success(team.getId());
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean save = teamService.removeById(id);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return ResultUtils.success(true);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
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
    @GetMapping("/list")
    public BaseResponse<List<Team>> getTeamLists(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        if (teamList == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamList);
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
}
