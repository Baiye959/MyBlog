package com.baiye959.myblog_backend.service;

import com.baiye959.myblog_backend.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author Baiye959
 */

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("Baiye959");
        user.setAvatarUrl("https://baiye959.cn/cat3.jpg");
        user.setUserPassword("Baiye959");
        user.setEmail("3383522774@qq.com");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        // 非空
        String username = "";
        String email = "email1@qq.com";
        String userPassword = "123456";
        long result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);
        username = "username1";
        email = "";
        userPassword = "123456";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);
        username = "username1";
        email = "email1@qq.com";
        userPassword = "";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);

        // 长度
        username = "12345678910";
        email = "email1@qq.com";
        userPassword = "123456";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);
        username = "username1";
        email = "email1@qq.com";
        userPassword = "123";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);

        // 用户名不包含特殊字符
        username = "hello !";
        email = "email1@qq.com";
        userPassword = "123456";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);

        // 用户名不重复
        username = "Baiye959";
        userPassword = "12345678";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);
        // 邮箱不重复
        username = "username1";
        email = "3383522774@qq.com";
        userPassword = "12345678";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertEquals(-1, result);

        // 成功
        username = "username1";
        email = "email1@qq.com";
        userPassword = "123456";
        result = userService.userRegister(username, email, userPassword);
        Assertions.assertTrue(result > 0);
    }
}