package com.baiye959.myblog_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baiye959.myblog_backend.mapper")
public class MyblogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogBackendApplication.class, args);
    }

}
