package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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


    /**
     * 设置中 更新用户的信息
     *
     * @param userId,userName,email,avatarUrl,request
     */
    User userSetting(Long userId,String userName,String email,String avatarUrl,HttpServletRequest request);

    /**
     * 管理员修改用户状态/权限
     * @param userId
     * @param userRole
     * @param userStatus
     * @param request
     * @return
     */
    User userUpdate(Long userId, Integer userRole, Integer userStatus, HttpServletRequest request);

    List<User> getAllUser();
}
