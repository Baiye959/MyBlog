package com.baiye959.myblog_backend.model.domain.response;

import com.baiye959.myblog_backend.model.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long id;

    private String content;

    private LocalDateTime createTime;

    private String username;

    private String avatarUrl;

    private Long blogId;
}
