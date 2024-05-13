package com.zhao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhao.usercenter.exception.BusinessException;
import com.zhao.usercenter.mapper.UserMapper;
import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhao.usercenter.common.ErrorCode.NULL_ERROR;
import static com.zhao.usercenter.common.ErrorCode.PARAMS_ERROR;
import static com.zhao.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    /**
     * 用户实现类
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @return 用户id
     * author long
     */
    @Resource
    private UserMapper userMapper;
    public static final String SALT = "zhaoqian";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }
       if (userAccount.length() < 4){
           throw new BusinessException(NULL_ERROR,"账户长度过短");
       }
       if (checkPassword.length() <8 || userPassword.length() <8){
           throw new BusinessException(NULL_ERROR,"密码长度过短");
       }
       if (planetCode.length()>5){
           throw new BusinessException(NULL_ERROR,"星球编号过长");
       }
        //账户不能包含特殊字符
        String str = "^[a-zA-Z_][a-zA-Z0-9_-]{5,15}$";
        Matcher matcher = Pattern.compile(str).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(NULL_ERROR,"账户不能包含特殊字符");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(NULL_ERROR,"两次输入密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count>0){
            throw new BusinessException(NULL_ERROR,"已存在改账户");
        }
        //星球编号不能重复
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("planetCode",planetCode);
        long qwCount = userMapper.selectCount(queryWrapper);
        if (qwCount>0){
            throw new BusinessException(NULL_ERROR,"已存在改星球编号");//表示游人注册了，返回-1
        }
        //对密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(NULL_ERROR,"保存到数据库失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(PARAMS_ERROR,"不能为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(NULL_ERROR,"账户长度过短");
        }
        if (userPassword.length() <8){
            throw new BusinessException(NULL_ERROR,"密码长度过长");
        }
        //账户不能包含特殊字符
        String str = "^[a-zA-Z_][a-zA-Z0-9_-]{5,15}$";
        Matcher matcher = Pattern.compile(str).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(NULL_ERROR,"账户不能包含特殊字符");
        }
        //对密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",newPassword);
        User user1 = userMapper.selectOne(queryWrapper);
        if (user1==null){
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(NULL_ERROR,"不存在该用户");
        }
        /**
         * 用户脱敏
         */
        User safetyUser = getSafetyUser(user1);
        /**
         * 设置登录态
         */
        request.getSession().setAttribute(USER_LOGIN_STATE,user1);

        return safetyUser;
    }

    /**
     *
     * @param user1 用户
     * @return 安全用户
     */
    @Override
    public User getSafetyUser(User user1){
        User safetyUser = new User();
        safetyUser.setId(user1.getId());
        safetyUser.setUsername(user1.getUsername());
        safetyUser.setUserAccount(user1.getUserAccount());
        safetyUser.setProfile(user1.getProfile());
        safetyUser.setAvatarUrl(user1.getAvatarUrl());
        safetyUser.setGender(user1.getGender());
        safetyUser.setPhone(user1.getPhone());
        safetyUser.setEmail(user1.getEmail());
        safetyUser.setUserRole(user1.getUserRole());
        safetyUser.setUserStatus(user1.getUserStatus());
        safetyUser.setCreateTime(user1.getCreateTime());
        safetyUser.setPlanetCode(user1.getPlanetCode());
        safetyUser.setTags(user1.getTags());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * @param tagNameList 前端传来的标签列表
     * @return 用户列表
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList){
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(PARAMS_ERROR);
        }
        /**
         * 这个是利用sql查询
         */
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        for (String tagName : tagNameList) {
//            queryWrapper.like("tags",tagName);
//        }
//        List<User> users = userMapper.selectList(queryWrapper);
//        return users.stream().map(this::getSafetyUser).toList();
        /**
         * 内存chaxun
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        /**
         * 这一段建议反复观看
         */
        return users.stream().filter(user -> {
        String userTags = user.getTags();
        if (StringUtils.isBlank(userTags)){
            return false;
        }
        Set<String> tempTagnameSet = gson.fromJson(userTags, new TypeToken<Set<String>>(){}.getType());
            /**
             * 反复观看
             * tempTagnameSet 为空的话，给它new HashSet<>()这样一个默认值
             */
            tempTagnameSet = Optional.ofNullable(tempTagnameSet).orElse(new HashSet<>());
        for (String tTagName : tagNameList) {
            if (!tempTagnameSet.contains(tTagName)){
                return false;
            }
        }
        return true;
        }).map(this::getSafetyUser).toList();

    }
}
