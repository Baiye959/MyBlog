package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.service.AnnouncementService;
import com.baiye959.myblog_backend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.baiye959.myblog_backend.contant.UserContant.ADMIN_ROLE;
import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 公告接口
 *
 * @author qiiiia
 */
@RestController
@RequestMapping("/announcement")

public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;


    @Resource
    private UserService userService;
    /**
     * 是否成功创建一个公告
     *
     * @param annoucement,request
     * @return bool
     */
    @PostMapping("/create")
    public BaseResponse<Long> addAnnouncement(@RequestBody Announcement annoucement,HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (annoucement == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = annoucement.getContent();
        if(StringUtils.isBlank(content)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        Long userId=user.getId();
//        Long userId = 1L;
        Long result = announcementService.addAnnouncement(userId,content);
        return ResultUtils.success(result);
    }

    /**
     * 是否成功将公告更新
     *
     * @param announcement,request
     * @return bool
     */
    @PostMapping("/update")
    public BaseResponse<Long> updateAnnouncement(@RequestBody Announcement announcement,HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户权限不够");
        }
        if(announcement == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = announcement.getContent();
        Long id = announcement.getId();
        if(id == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        Long userId = user.getId();
//         long userId = 1;
        Long result = announcementService.updateAnnouncement(id,userId, content);
        return ResultUtils.success(result);
    }

    /**
     * 是否成功将公告删除
     * @param announcement
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = announcement.getId();
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b=announcementService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/getAll")
    public BaseResponse<List<Announcement>> getAllAnnouncement() {
        List<Announcement> announcementList=announcementService.getAllAnnouncement();
        return ResultUtils.success(announcementList);
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
