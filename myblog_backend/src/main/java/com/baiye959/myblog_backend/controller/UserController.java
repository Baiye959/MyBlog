package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.request.UserLoginRequest;
import com.baiye959.myblog_backend.model.domain.request.UserRegisterRequest;
import com.baiye959.myblog_backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiye959.myblog_backend.contant.UserContant.ADMIN_ROLE;
import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Baiye959
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getUserPassword();
        String email = userRegisterRequest.getEmail();
        if (StringUtils.isAnyBlank(username, userPassword, email)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = userService.userRegister(username, email, userPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginRequest.getEmail();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(email, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(email, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/setting")
    public BaseResponse<User> userSetting(@RequestBody User user, HttpServletRequest request){
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息为空");
        }
        String userName = user.getUsername();
        String email = user.getEmail();
        String avatarUrl = user.getAvatarUrl();

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long userId = currentUser.getId();

        User newUser = userService.userSetting(userId,userName,email,avatarUrl,request);
        return ResultUtils.success(newUser);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(users);
    }

    @PostMapping("/update")
    public BaseResponse<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 仅管理员可更改用户状态/权限
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"无权限更改用户状态信息");
        }

        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更改用户信息为空");
        }
        Integer userStatus = user.getUserStatus();
        Integer userRole = user.getUserRole();
        long userId = user.getId();

        User newUser = userService.userUpdate(userId,userRole,userStatus,request);
        return ResultUtils.success(newUser);
    }

    @GetMapping("/getAll")
    public BaseResponse<List<User>> getAllUser() {
        List<User> userList=userService.getAllUser();
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody User user, HttpServletRequest request) {
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        Long id = user.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
