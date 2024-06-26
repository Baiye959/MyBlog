package com.baiye959.myblog_backend.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Baiye959
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -280182165593738072L;
    private String username;
    private String email;
    private String userPassword;
}
