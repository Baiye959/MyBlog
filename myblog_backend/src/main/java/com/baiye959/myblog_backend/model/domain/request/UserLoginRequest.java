package com.baiye959.myblog_backend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -44258826960016000L;
    private String email;
    private String userPassword;
}
