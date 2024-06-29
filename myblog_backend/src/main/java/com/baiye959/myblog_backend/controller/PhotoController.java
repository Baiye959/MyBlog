package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baiye959.myblog_backend.model.domain.Photo;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.response.BlogResponse;
import com.baiye959.myblog_backend.service.PhotoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/photo")
public class PhotoController {
    @Resource
    private PhotoService photoService;

    @GetMapping("/getMyPhoto")
    public BaseResponse<List<Photo>> getMyPhoto(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        long userId = currentUser.getId();
        List<Photo> photos = photoService.getMyPhoto(userId);
        return ResultUtils.success(photos);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAnnouncement(@RequestBody Long id, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        long userId = currentUser.getId();

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的ID");
        }

        // 获取要删除的照片
        Photo photo = photoService.getById(id);
        if (photo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "照片未找到");
        }

        // 检查照片的userId是否匹配
        if (photo.getUserId() != userId) {
            throw new BusinessException(ErrorCode.NO_AUTH, "不能删除别人的照片");
        }
        boolean b = photoService.removeById(id);
        return ResultUtils.success(b);
    }
}
