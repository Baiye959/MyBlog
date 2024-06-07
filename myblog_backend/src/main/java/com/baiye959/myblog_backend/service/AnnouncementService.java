package com.baiye959.myblog_backend.service;


import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AnnouncementService extends IService<Announcement> {
    /**
     * 添加公告
     *
     * @param  userId, content
     * @return announcementId
     */
    Long addAnnouncement(Long userId,String content);
    /**
     * 更新公告
     *
     * @param id,userId,content
     * @return announcementId
     */
    Long updateAnnouncement(Long id,Long userId, String content);
    /**
     * 得到所有公告的信息
     *
     * @param
     * @return List<Announcement>
     */
    List<Announcement> getAllAnnouncement();
}
