package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author Baiye959
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param username 用户名
     * @param email 用户邮箱
     * @param userPassword 用户密码
     * @return
     */
    Long userRegister(String username, String email, String userPassword);

    /**
     * 用户登录
     *
     * @param email  用户邮箱
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String email, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     */
    Integer userLogout(HttpServletRequest request);
}
