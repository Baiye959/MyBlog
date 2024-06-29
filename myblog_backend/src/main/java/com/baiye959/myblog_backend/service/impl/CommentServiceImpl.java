package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.CommentMapper;
import com.baiye959.myblog_backend.mapper.UserMapper;
import com.baiye959.myblog_backend.model.domain.Comment;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.response.CommentResponse;
import com.baiye959.myblog_backend.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 查询当前博客下的评论
     * @param blogId
     * @return
     */
    @Override
    public List<CommentResponse> getComments(Long blogId) {
        // 查询评论
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getBlogId, blogId);
        List<Comment> comments = commentMapper.selectList(commentWrapper);

        List<CommentResponse> commentList = new ArrayList<>();

        // 查询关联实体
        for (Comment comment : comments) {
            Long userId = comment.getUserId();
            // 查询与当前评论相关的用户信息
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getId, userId);
            User userInfo = userMapper.selectOne(userWrapper);
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            commentResponse.setContent(comment.getContent());
            commentResponse.setCreateTime(comment.getCreateTime());
            commentResponse.setParentCommentId(comment.getParentCommentId());
            commentResponse.setUser(userInfo);
            commentResponse.setBlogId(blogId);
            commentList.add(commentResponse);
        }

        return commentList;
    }

    /**
     * 添加评论
     * @param userId
     * @param blogId
     * @param parentCommentId
     * @param content
     * @param time1
     * @return
     */
    @Override
    public Long addComment(long userId, long blogId, long parentCommentId, String content, LocalDateTime time1){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setBlogId(blogId);
        comment.setParentCommentId(parentCommentId);
        comment.setContent(content);
        comment.setCreateTime(time1);
        boolean saveResult = this.save(comment);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return comment.getId();
    }

    /**
     * 查询评论用户和试图删除评论的用户是否为同一人，若是则返回true
     * @param commentId
     * @param userId
     * @return
     */
    public boolean isMyComment(long commentId, long userId){
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getId, commentId);
        Comment comment = commentMapper.selectOne(commentWrapper);
        if(comment.getUserId() != userId){
            return false;
        }
        return true;
    }
}
