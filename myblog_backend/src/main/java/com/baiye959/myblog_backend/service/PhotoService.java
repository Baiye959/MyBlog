package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.response.BlogResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baiye959.myblog_backend.model.domain.Photo;

import java.util.List;

/**
* @author 33835
* @description 针对表【photo】的数据库操作Service
* @createDate 2024-06-29 22:51:19
*/
public interface PhotoService extends IService<Photo> {

    Long addPhoto(Long userId, String photoUrl);

    List<Photo> getMyPhoto(long userId);
}
