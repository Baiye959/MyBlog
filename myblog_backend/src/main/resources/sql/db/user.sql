-- 注册新用户
CREATE PROCEDURE register(
    IN p_userName VARCHAR(256), 
    IN p_email VARCHAR(512),
    IN p_password VARCHAR(512),
    OUT UserId BIGINT
)
BEGIN
    DECLARE temp_count INT;
    
    -- 检查用户名、邮箱和密码是否为空
    IF p_username IS NULL OR p_email IS NULL OR p_password IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '用户名、邮箱和密码不能为空';
    END IF;
    
    -- 检查用户名是否包含特殊字符
    IF p_username REGEXP '[^a-zA-Z0-9_]' THEN
        SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = '用户名包含特殊字符';
    END IF;
    
    -- 检查用户名长度是否超过50
    IF CHAR_LENGTH(p_username) > 50 THEN
        SIGNAL SQLSTATE '45002' SET MESSAGE_TEXT = '用户名长度超过50';
    END IF;
    
    -- 检查密码是否小于6位
    IF CHAR_LENGTH(p_password) < 6 THEN
        SIGNAL SQLSTATE '45003' SET MESSAGE_TEXT = '密码长度小于6位';
    END IF;
    
    -- 检查邮箱和用户名是否重复
    SELECT COUNT(*) INTO temp_count FROM user 
    WHERE username = p_userName OR email = p_email;
    IF temp_count > 0 THEN
        SIGNAL SQLSTATE '45003' SET MESSAGE_TEXT = '用户名或邮箱已存在';
    END IF;
    
    -- 插入新用户
    INSERT INTO user (username, userPassword, email)
    VALUES (p_userName, p_password, p_email);
    SELECT LAST_INSERT_ID() AS UserId;
END

-- 用户登录
CREATE PROCEDURE login(
    IN p_email VARCHAR(512), 
    IN p_password VARCHAR(512)
)
BEGIN
    DECLARE temp_userId INT;
    DECLARE temp_password INT;
    
    -- 检查邮箱和密码是否为空，为空则拒绝登录
    IF p_email IS NULL OR p_password IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '邮箱和密码不能为空';
    END IF;
    
    -- 检查用户是否存在和密码是否匹配
    SELECT temp_userId, userPassword INTO temp_userId, temp_password
    FROM user
    WHERE email = p_email;

    -- 未注册则拒绝登录
    IF temp_userId IS NULL THEN
        SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = '用户不存在';
    END IF;
    
    -- 不匹配则拒绝登录
    IF temp_password != password THEN
        SIGNAL SQLSTATE '45002' SET MESSAGE_TEXT = '密码错误';
    END IF;
    
    -- 登录成功，返回用户信息
    CREATE VIEW user_view AS 
    SELECT * FROM user WHERE id = p_userId;
END;

-- 修改用户信息
CREATE PROCEDURE setUser(
    IN p_userId BIGINT,
    IN p_userName VARCHAR(256),
    IN p_email VARCHAR(512),
    IN p_password VARCHAR(512),
    IN p_avatarUrl VARCHAR(1024)
)
BEGIN
    IF p_userName IS NULL AND p_email IS NULL AND p_password IS NULL AND p_avatarUrl IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '请输入修改信息';
    ELSE
        UPDATE user
        SET username = p_userName, email = p_email, password = p_password, avatarUrl = p_avatarUrl
        WHERE id = p_userId;

        CREATE VIEW user_view AS 
        SELECT * FROM user WHERE id = p_userId;
    END IF;
END

-- 更新用户状态
CREATE PROCEDURE updateUser(
    IN p_userId BIGINT,
    IN p_userRole INT,
    IN p_userStatus INT
)
BEGIN
    UPDATE user
    SET userRole = p_userRole, userStatus = p_userStatus
    WHERE id = p_userId;

    CREATE VIEW user_view AS 
    SELECT * FROM user WHERE id = p_userId;
END

-- 删除用户
CREATE PROCEDURE deleteUser(
    IN p_userId BIGINT, 
    OUT Result BOOLEAN
)
BEGIN
    DECLARE temp_user_role INT;

    -- 管理员才能删除用户
    SELECT userRole INTO temp_user_role
    FROM user
    WHERE id = p_userId;

    IF temp_user_role = 1 THEN
        DELETE FROM user
        WHERE id = p_userId;

        SET Result = TRUE;
    ELSE
        SET Result = FALSE;
    END IF;
END

-- 查询所有用户（视图）
CREATE VIEW user_view AS
SELECT * FROM user;