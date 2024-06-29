package com.baiye959.myblog_backend.model.domain.response;


import com.baiye959.myblog_backend.model.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private User user;
}
