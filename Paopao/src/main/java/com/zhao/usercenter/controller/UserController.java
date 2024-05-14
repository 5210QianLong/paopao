package com.zhao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.zhao.usercenter.common.BaseResponse;
import com.zhao.usercenter.common.ResultUtils;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.model.requset.userLoginRequset;
import com.zhao.usercenter.model.requset.userRegisterRequset;
import com.zhao.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.zhao.usercenter.common.ErrorCode.*;
import static com.zhao.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户请求接口
 * RequestMapping("/user") 是接口路径
 * swagger开发文档 knife4j增强 <a href="http://localhost:8080/api/doc.html"/> 查看文档
 * <p>
 * 注解@Profile("dev") 防止接口消息暴露，dev 默认配置，prod 加载上线配置yml
 * @author zql
 */
@RestController
@RequestMapping("/user")
@ApiSupport(author = "zql")
@Profile("dev")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody userRegisterRequset userRegisterRequset) {
        if (userRegisterRequset == null) {
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }

        String userAccount = userRegisterRequset.getUserAccount();
        String userPassword = userRegisterRequset.getUserPassword();
        String checkPassword = userRegisterRequset.getCheckPassword();
        String planetCode = userRegisterRequset.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody userLoginRequset userLoginRequset, HttpServletRequest request) {
        if (userLoginRequset == null) {
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }
        String userAccount = userLoginRequset.getUserAccount();
        String userPassword = userLoginRequset.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(NOT_LOGIN,"会话已失效,未登录");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     *
     * @param user 前端来的数据
     * @param request 获取当前用户登录态
     * @return 是否成功
     */
    @PostMapping("/update")
    public BaseResponse<Integer> userUpdate(User user,HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Integer userInfo = userService.updateUserInfo(user,loginUser);
        return ResultUtils.success(userInfo);
    }

    /**
     * 用户查询接口
     * 注意，只有管理员采用权限使用该接口
     *
     */

    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)){
            throw new BusinessException(NOT_AUTH,"无权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);

        //由于上述是直接调用baseMapper里的方法，没有经过safety user的处理
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).toList();
        return ResultUtils.success(result);
    }

    /**
     *
     * @param tagNameList 前端传来的标签列表
     * @return 满足要求的用户列表
     *
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(NULL_ERROR,"传参为空");
        }
        return ResultUtils.success(userService.searchUserByTags(tagNameList));
    }
    /**
     * 注意，只有管理员采用权限使用该接口
     *
     * @param id 前端传来的用户id
     * @return 返回是否已逻辑删除
     */

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long id,HttpServletRequest request) {
        if (!userService.isAdmin(request)){
            throw new BusinessException(NOT_AUTH,"无权限");
        }
        if (id <= 0) {
            throw new BusinessException(NULL_ERROR,"id不存在");
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

}
