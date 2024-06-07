package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.AnnouncementMapper;
import com.baiye959.myblog_backend.mapper.UserMapper;
import com.baiye959.myblog_backend.model.domain.Announcement;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.service.AnnouncementService;
import com.baiye959.myblog_backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * @description 针对表【announcement(公告)】的数据库操作Service实现
 * @createDate 2024-06-02 19:36:15
 */
@Service
@Slf4j
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public Long addAnnouncement(Long userId,String content) {
        if(StringUtils.isAnyBlank(content)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"内容为空");
        }

        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setCreateTime(new Date());
        announcement.setUpdateTime(new Date());
        announcement.setUserId(userId);
        boolean save = this.save(announcement);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return announcement.getId();
    }

    @Override
    public Long updateAnnouncement(Long id,Long userId, String content){
        if(StringUtils.isAnyBlank(content)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"内容为空");
        }
        Announcement announcement = this.getById(id);
        if(announcement.getUserId()!=userId){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发布公告者与更改公告者不相同");
        }
        announcement.setContent(content);
        boolean update = this.updateById(announcement);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改公告失败");
        }
        return announcement.getId();
    }

    @Override
    public List<Announcement> getAllAnnouncement() {
        List<Announcement> announcements = announcementMapper.selectList(null);
        return announcements;
    }

}
