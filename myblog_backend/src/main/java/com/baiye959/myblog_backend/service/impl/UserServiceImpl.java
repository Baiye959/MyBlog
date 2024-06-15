package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.UserMapper;
import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.print.DocFlavor;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author Baiye959
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-06-02 19:36:15
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "baiye959";

    @Override
    public Long userRegister(String username, String email, String userPassword) {
        // 1. 校验
        // 均不为空
        if (StringUtils.isAnyBlank(username, userPassword, email)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        // 长度校验
        if (username.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名太长");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码太短");
        }
        // 用户名不能包含特殊字符
        String validPattern = "\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能包含特殊字符");
        }
        // 用户名不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名重复");
        }
        // 邮箱不重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱重复");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入
        User user = new User();
        user.setUserPassword(encryptPassword);
        user.setEmail(email);
        user.setUsername(username);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String email, String userPassword, HttpServletRequest request) {
        // 1. 校验
        // 均不为空
        if (StringUtils.isAnyBlank(email, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        // 长度校验
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码太短");
        }

        // 2. 密码是否正确
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, email or userPassword cannot match");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱或密码错误");
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        // 5. 返回脱敏后的用户信息
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 设置中 更新用户的信息
     *
     * @param userName,email,avatarUrl,request
     */
    @Override
    public User userSetting(Long userId,String userName, String email, String avatarUrl,HttpServletRequest request){
        User currentUser = getById(userId);
        if(!StringUtils.isAnyBlank(userName)){
            currentUser.setUsername(userName);
        }
        if(!StringUtils.isAnyBlank(email)){
            currentUser.setEmail(email);
        }
        if(!StringUtils.isAnyBlank(avatarUrl)){
            currentUser.setAvatarUrl(avatarUrl);
        }

        boolean b= this.updateById(currentUser);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return getSafetyUser(currentUser);
    }

    @Override
    public User userUpdate(Long userId, Integer userRole, Integer userStatus, HttpServletRequest request){
        User currentUser = getById(userId);
        currentUser.setUserStatus(userStatus);
        currentUser.setUserRole(userRole);

        boolean b= this.updateById(currentUser);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return getSafetyUser(currentUser);
    }

    @Override
    public List<User> getAllUser(){
        List<User> users = userMapper.selectList(null);
        return users;
    }
}




