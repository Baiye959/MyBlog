package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.Comment;
import com.baiye959.myblog_backend.model.domain.response.CommentResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService extends IService<Comment> {

    List<CommentResponse> getComments(Long blogId);

    Long addComment(long userId, long blogId, long parentCommentId, String content, LocalDateTime time1);

    boolean isMyComment(long commentId, long userId);
}
