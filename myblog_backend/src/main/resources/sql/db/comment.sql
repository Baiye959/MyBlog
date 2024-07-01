-- 添加评论
CREATE PROCEDURE addComment(
    IN p_userId BIGINT, 
    IN p_blogId BIGINT,
    IN p_content VARCHAR(10000)
    OUT CommentId BIGINT
)
BEGIN
    -- 评论内容为空则不追加
    IF (p_content IS NOT NULL) THEN
        INSERT INTO comment(content, userId, blogId) 
        VALUES (p_content, p_userId, p_blogId);
    END IF;
    
    SELECT LAST_INSERT_ID() AS CommentId;
END;

-- 删除评论
CREATE PROCEDURE deleteComment(
  IN p_commentId BIGINT, 
  IN p_userId BIGINT, 
  OUT Result BOOLEAN
)
BEGIN
  DECLARE temp_user_id BIGINT;
  -- 只能删除本人的评论
  SELECT userId INTO temp_user_id
  FROM comment
  WHERE id = p_commentId;
  
  IF temp_user_id = p_userId THEN
    DELETE FROM comment
    WHERE id = p_commentId;
    
    SET Result = TRUE;
  ELSE
    SET Result = FALSE;
  END IF;
END

-- 查询特定博客下评论（返回对应用户名，头像，评论内容和发布时间的视图）
CREATE PROCEDURE getComment(
  IN p_blogId BIGINT, 
)
BEGIN
    CREATE VIEW comment_view3(userName, avatarUrl, content, createTime)
    AS
    SELECT u.username, u.avatarUrl, c.content, c.createTime
    FROM comment c
    JOIN user u ON u.id = c.userId;

    SELECT userName, avatarUrl, content, createTime
    FROM comment_view
    WHERE blogId = p_blogId;
END
