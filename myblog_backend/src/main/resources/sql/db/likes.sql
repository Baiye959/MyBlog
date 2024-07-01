-- 点赞和取消点赞
CREATE PROCEDURE addLike(
    IN p_userId BIGINT, 
    IN p_blogId BIGINT,
)
BEGIN
    SELECT COUNT(*) AS count 
    FROM likes 
    WHERE userId = p_userId AND blog_id = p_blogId;

    IF count > 0 THEN   --取消点赞
        DELETE FROM likes WHERE userId = p_userId AND blog_id = p_blogId;
    ELSE
        INSERT INTO likes(userId, blogId) VALUES(p_userId, p_blogId);
    END IF;

END;

-- 查询特定博客下点赞数量（视图）
CREATE PROCEDURE getLikes(
  IN p_blogId BIGINT, 
)
BEGIN
    CREATE VIEW likes_view AS
    SELECT COUNT(*) FROM likes WHERE blogId = p_blogId;
END
