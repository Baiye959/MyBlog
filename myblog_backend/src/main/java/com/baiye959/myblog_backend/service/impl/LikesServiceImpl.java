package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.LikesMapper;
import com.baiye959.myblog_backend.model.domain.Likes;
import com.baiye959.myblog_backend.service.LikesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes>
        implements LikesService {

    @Resource
    private LikesMapper likesMapper;

    @Override
    public long getLikesCount(Long blogId){
        LambdaQueryWrapper<Likes> likesWrapper = new LambdaQueryWrapper<>();
        likesWrapper.eq(Likes::getBlogId, blogId);
        return likesMapper.selectCount(likesWrapper);
    }

    @Override
    public boolean searchLike(long userId, Long blogId){
        LambdaQueryWrapper<Likes> likesWrapper = new LambdaQueryWrapper<>();
        likesWrapper.eq(Likes::getBlogId, blogId)
                .eq(Likes::getUserId, userId);
        long count = likesMapper.selectCount(likesWrapper);
        if (count > 0) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addLike(long userId, long blogId){
        Likes likes = new Likes();
        likes.setUserId(userId);
        likes.setBlogId(blogId);
        boolean saveResult = this.save(likes);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return true;
    }

    @Override
    public boolean cancelLike(long userId, long blogId){
        LambdaQueryWrapper<Likes> likesWrapper = new LambdaQueryWrapper<>();
        likesWrapper.eq(Likes::getBlogId, blogId)
                .eq(Likes::getUserId, userId);
        long count = likesMapper.selectCount(likesWrapper);

        if (count > 0) {
            int deleted = likesMapper.delete(likesWrapper);
            return (deleted == count);     // deleted为被成功删除的记录数
        }
        return true;
    }

}
