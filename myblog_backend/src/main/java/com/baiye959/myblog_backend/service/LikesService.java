package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.Likes;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LikesService extends IService<Likes> {

    boolean searchLike(long userId, Long blogId);

    long getLikesCount(Long blogId);

    boolean addLike(long userId, long blogId);

    boolean cancelLike(long userId, long blogId);
}
