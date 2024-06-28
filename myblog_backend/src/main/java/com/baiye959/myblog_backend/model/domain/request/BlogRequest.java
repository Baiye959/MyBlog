package com.baiye959.myblog_backend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlogRequest {
    private Long id;
    private String title;
    private String content;

}
