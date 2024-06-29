package com.baiye959.myblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baiye959.myblog_backend.model.domain.Photo;
import com.baiye959.myblog_backend.mapper.PhotoMapper;
import com.baiye959.myblog_backend.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 33835
* @description 针对表【photo】的数据库操作Service实现
* @createDate 2024-06-29 22:51:19
*/
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
    implements PhotoService{

    @Override
    public Long addPhoto(Long userId, String photoUrl) {
        Photo photo = new Photo();
        photo.setUserId(userId);
        photo.setPhotoUrl(photoUrl);
        this.save(photo);
        return photo.getId();
    }


    @Override
    public List<Photo> getMyPhoto(long userId) {
        return this.lambdaQuery().eq(Photo::getUserId, userId).list();
    }
}




