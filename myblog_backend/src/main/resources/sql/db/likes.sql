-- 点赞和取消点赞
DELIMITER //
CREATE PROCEDURE addLike(
    IN p_userId BIGINT, 
    IN p_blogId BIGINT
)
BEGIN
    DECLARE count INT;

    SELECT COUNT(*) INTO count
    FROM likes 
    WHERE userId = p_userId AND blogId = p_blogId;
    -- 取消点赞
    IF count > 0 THEN
        DELETE FROM likes WHERE userId = p_userId AND blogId = p_blogId;
    -- 点赞
    ELSE
        INSERT INTO likes(userId, blogId) VALUES(p_userId, p_blogId);
    END IF;
END//
DELIMITER ;

CALL addLike(@userId, @blogId);
-- -------------------------------------------------------------------------
-- 查询特定博客下点赞数量（视图）
CREATE PROCEDURE getLikes(
  IN p_blogId BIGINT
)
BEGIN
    CREATE VIEW likes_view AS
    SELECT COUNT(*) FROM likes WHERE blogId = p_blogId;
END;

CALL getLikes(@blogId);

