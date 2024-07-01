-- 添加公告
DELIMITER //
CREATE PROCEDURE addAnnouncement(
    IN p_userId BIGINT,
    IN p_announcementContent VARCHAR(10000),
    OUT AnnouncementId BIGINT
)
BEGIN
    DECLARE userRole INT;
    DECLARE announcementId BIGINT;

    -- 如果公告内容为空，拒绝修改
    IF p_announcementContent = '' OR p_announcementContent IS NULL
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '公告内容不能为空';
    END IF;

    -- 检查用户是否为管理员
    SELECT userRole INTO userRole FROM user WHERE id = p_userId;

    -- 如果用户不是管理员，则拒绝修改
    IF userRole = 0 THEN
        SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = '无权限';
    ELSE
        -- 如果用户是管理员，则插入公告
        INSERT INTO announcement (userId, content, createTime, updateTime)
        VALUES (p_UserId, p_announcementContent, NOW(), NOW());

        -- 返回插入的公告id值
        SELECT LAST_INSERT_ID() AS AnnouncementId;
    END IF;
END//
DELIMITER ;

CALL addAnnouncement(@userId, @content, @id);
SELECT @id;
-- -----------------------------------------------
-- 更新公告
DELIMITER //
CREATE PROCEDURE updateAnnouncement(
    IN p_announcementId BIGINT,
    IN p_userId BIGINT,
    IN p_announcementContent VARCHAR(10000),
    OUT AnnouncementId BIGINT
)
BEGIN
    DECLARE temp_user_id INT;

    -- 获取现有公告的用户id
    SELECT userId INTO temp_user_id
    FROM announcement
    WHERE id = p_announcementId;

    -- 如果公告内容为空，拒绝修改
    IF p_announcementContent = '' OR p_announcementContent IS NULL
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '公告内容不能为空';
    END IF;

    -- 如果用户id和当前表内公告id相同的公告的用户id不同，拒绝修改
    IF temp_user_id != p_userId
    THEN
        SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = '只有原作者才能修改公告';
    END IF;

    -- 修改公告内容和更新时间
    UPDATE announcement
    SET content = p_announcementContent,
        updateTime = NOW()
    WHERE id = p_announcementId;

    -- 返回公告id值
    SELECT p_announcementId AS AnnouncementId;
END//
DELIMITER ;

CALL updateAnnouncement(@in_id, @userId, @content, @out_id);
SELECT @out_id;
-- --------------------------------------------------------------------
-- 删除公告
DELIMITER //
CREATE PROCEDURE deleteAnnouncement(
    IN p_announcementId BIGINT,
    IN p_userId INT,
    OUT Result BOOLEAN
)
BEGIN
    DECLARE temp_user_id INT;

    SELECT userId INTO temp_user_id
    FROM announcement
    WHERE id = p_announcementId;

    IF temp_user_id = p_userId THEN
        DELETE FROM announcement
        WHERE id = p_announcementId;

        SET Result = TRUE;
    ELSE
        SET Result = FALSE;
    END IF;
END//
DELIMITER ;

CALL deleteAnnouncement(@in_id, @userId, @result);
SELECT @result;
-- ---------------------------------------------
-- 查询所有公告（视图）
CREATE VIEW announcement_view AS
SELECT * FROM announcement;