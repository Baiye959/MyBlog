-- 上传图片
CREATE PROCEDURE addPhoto(
    IN p_userId BIGINT, 
    IN p_photoUrl VARCHAR(255),
    OUT PhotoId BIGINT
)
BEGIN
    INSERT INTO photo(userId, photoUrl) 
    VALUES (p_userId, p_photoUrl);
    
    SELECT LAST_INSERT_ID() AS PhotoId;
END;

CALL addPhoto(@userId, @photoUrl, @id);
SELECT @id;
-- --------------------------------------------------------------------
-- 删除图片
DELIMITER //
CREATE PROCEDURE deleteComment(
  IN p_photoId BIGINT, 
  IN p_userId BIGINT, 
  OUT Result BOOLEAN
)
BEGIN
  DECLARE temp_user_id BIGINT;
  -- 只能删除本人上传的图片
  SELECT userId INTO temp_user_id
  FROM photo
  WHERE id = p_photoId;
  
  IF temp_user_id = p_userId THEN
    DELETE FROM photo
    WHERE id = p_photoId;
    
    SET Result = TRUE;
  ELSE
    SET Result = FALSE;
  END IF;
END//
DELIMITER ;

CALL deleteComment(@photoId, @userId, @result);
SELECT @result;
-- --------------------------------------------------------------------------
-- 查询自己的相册（视图）
CREATE PROCEDURE getComment(
  IN p_userId BIGINT
)
BEGIN
    CREATE VIEW photo_view AS
    SELECT * FROM photo WHERE userId = p_userId;
END;

CALL getComment(@userId);
