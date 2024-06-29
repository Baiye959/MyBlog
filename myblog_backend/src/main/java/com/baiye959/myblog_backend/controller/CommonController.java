package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.service.PhotoService;
import com.baiye959.myblog_backend.utils.AliOssUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Resource
    private PhotoService photoService;

    @PostMapping("/upload")
    public BaseResponse<Long> upload(MultipartFile file, HttpServletRequest request){
        log.info("文件上传");
        // 获取上传的文件的原始名称
        String originalFilename = file.getOriginalFilename();
        // 获取上传的文件的后缀名，例如asd.png的.png后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 根据uuid创建新文件名
        String fileName = UUID.randomUUID().toString()+substring;
        // 使用aliOssUtil的upload方法
        try {
            String uploadPath = aliOssUtil.upload(file.getBytes(), fileName);
            // 返回文件上传路径
//            return ResultUtils.success(uploadPath);

            Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
            User user = (User) userObj;
            Long userId = user.getId();
            Long result = photoService.addPhoto(userId, uploadPath);
            return ResultUtils.success(result);
        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
//            e.printStackTrace();
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }
}