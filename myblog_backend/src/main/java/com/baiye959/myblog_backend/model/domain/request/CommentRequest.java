package com.baiye959.myblog_backend.model.domain.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long blogId;
    private String content;
}
