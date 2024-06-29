package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.Blog;
import com.baiye959.myblog_backend.model.domain.response.BlogResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客服务
 * @author zzzduo
 */
public interface BlogService extends IService<Blog> {
    /**
     * 返回指定用户的博客信息
     * @param userId
     * @return
     */
    List<BlogResponse> getMyBlog(long userId);

    BlogResponse getOneBlog(Long id);

    List<BlogResponse> getAllBlog();

    Long addBlog(long userId, String title, String content, LocalDateTime time1, LocalDateTime time2);


    Long updateBlog(long id, String title, String content, LocalDateTime time2);
}
