package com.zhao.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.usercenter.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * @param userAccount  账户
     * @param userPassword 密码
     * @param request 用于登录态使用
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     *用户脱敏接口
     * @param user1 用户
     * @return 安全用户对象
     */
    User getSafetyUser(User user1);

    int userLogout(HttpServletRequest request);

    /**
     * 根据标签查询用户接口
     * @param tagNameList 标签列表
     * @return 用户列表
     */
    List<User> searchUserByTags(List<String> tagNameList);
    /**
     *
     * @param user 前端传来的用户数据
     * @param loginUser 通过session得到当前的用户登录态
     * @return 是否成功修改
     */
    Integer updateUserInfo(User user,User loginUser);

    /**
     *
     * @param request 得到当前登录态用户信息
     * @return 登录的用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 鉴权
     * @param request 通过session获得用户登录态
     * @return 是否登录
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 鉴权方法的重载
     * @param userLogin 传入一个 通过session 获得的用户信息
     * @return 是否是管理员
     */
    boolean isAdmin(User userLogin);
}
