-- 添加博客
DELIMITER //
CREATE PROCEDURE addBlog(
    IN p_userId BIGINT,
    IN p_blogTitle VARCHAR(100),
    IN p_blogContent VARCHAR(10000),
    OUT BlogId BIGINT
)
BEGIN
    IF (p_blogTitle IS NOT NULL AND p_blogContent IS NOT NULL) THEN
        INSERT INTO blog(userId, title, content) VALUES (p_userId, p_blogTitle, p_blogContent);
    END IF;

    SELECT LAST_INSERT_ID() AS BlogId;
END//
DELIMITER ;

CALL addBlog(@userId, @title, @content, @blogId);
SELECT @blogId;
-- -----------------------------------------------------------
-- 编辑博客
DELIMITER //
CREATE PROCEDURE updateBlog(
    IN p_blogId BIGINT,
    IN p_blogTitle VARCHAR(100),
    IN p_blogContent VARCHAR(10000),
    OUT BlogId BIGINT
)
BEGIN
    DECLARE temp_user_id INT;

    -- 如果博客内容为空，拒绝修改
    IF p_blogContent = '' OR p_blogContent IS NULL
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '博客内容不能为空';
    END IF;

    -- 修改博客内容和更新时间
    UPDATE blog
    SET title = p_blogTitle,
        content = p_blogContent,
        updateTime = NOW()
    WHERE id = p_blogId;

    -- 返回博客id值
    SELECT p_blogId AS BlogId;
END//
DELIMITER ;

CALL updateBlog(@in_id, @title, @content, @out_id);
SELECT @out_id;
-- ------------------------------------------------------
-- 本人删除博客
DELIMITER //
CREATE PROCEDURE deleteBlog1(
    IN p_blogId BIGINT,
    IN p_userId BIGINT,
    OUT Result BOOLEAN
)
BEGIN
    DECLARE temp_user_id BIGINT;

    SELECT userId INTO temp_user_id
    FROM blog
    WHERE id = p_blogId;

    IF temp_user_id = p_userId THEN
        DELETE FROM blog
        WHERE id = p_blogId;

        SET Result = TRUE;
    ELSE
        SET Result = FALSE;
    END IF;
END//
DELIMITER ;

CALL deleteBlog1(@blogId, @userId, @result);
SELECT @result;
-- -----------------------------------------------------
-- 管理员删除博客
DELIMITER //
CREATE PROCEDURE deleteBlog2(
    IN p_blogId BIGINT,
    IN p_userId INT,
    OUT Result BOOLEAN
)
BEGIN
    DECLARE temp_user_role INT;

    SELECT userRole INTO temp_user_role
    FROM user
    WHERE id = p_userId;

    IF temp_user_role = 1 THEN
        DELETE FROM blog
        WHERE id = p_blogId;

        SET Result = TRUE;
    ELSE
        SET Result = FALSE;
    END IF;
END//
DELIMITER ;

CALL deleteBlog2(@blogId, @userId, @result);
SELECT @result;
-- ----------------------------------------------------------
-- 查询所有博客（视图）
CREATE VIEW blog_view1 AS
SELECT * FROM blog;
-- ------------------------------------------------------------
-- 查询本人博客（视图）
CREATE PROCEDURE getBlog1(
    IN p_userId BIGINT
)
BEGIN
    CREATE VIEW blog_view2 AS
    SELECT * FROM blog WHERE userId = p_userId;
END;

CALL getBlog1(@blogId);
-- ------------------------------------------------------------
-- 查询单条博客（返回对应用户名，头像，博客标题，内容和发布时间的视图）
CREATE PROCEDURE getBlog2(
    IN p_blogId BIGINT
)
BEGIN
    CREATE VIEW blog_view3(blogId, userName, avatarUrl, title, content, createTime)
    AS
    SELECT b.id, u.username, u.avatarUrl, b.title, b.content, b.createTime
    FROM blog b
             JOIN user u ON u.id = b.userId;

    SELECT userName, avatarUrl, title, content, createTime
    FROM blog_view3
    WHERE blogId = p_blogId;
END;

CALL getBlog2(@blogId);