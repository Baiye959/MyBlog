package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.request.CommentRequest;
import com.baiye959.myblog_backend.model.domain.response.CommentResponse;
import com.baiye959.myblog_backend.service.CommentService;
import com.baiye959.myblog_backend.service.LikesService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 点赞评论接口
 * @author zzzduo
 */
@RestController
@RequestMapping("/intercation")
public class InteractionController {

    @Resource
    private CommentService commentService;
    private LikesService likesService;

    /**
     * 查询特定博客下的评论
     * @param blogId
     * @return
     */
    @GetMapping("/getComment")
    public BaseResponse<List<CommentResponse>> getComment(@RequestParam("id") Long blogId) {
        List<CommentResponse> commentList=commentService.getComments(blogId);
        return ResultUtils.success(commentList);
    }

    @PostMapping("/addComment")
    public BaseResponse<Long> addComment(@RequestBody CommentRequest commentRequest, HttpServletRequest request){
        if (commentRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = currentUser.getId();
        long blogId = commentRequest.getBlogId();
        long parentCommentId = commentRequest.getParentCommentId();
        String content = commentRequest.getContent();
        LocalDateTime time1 = LocalDateTime.now();
        if (StringUtils.isAnyBlank(content)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = commentService.addComment(userId, blogId, parentCommentId, content, time1);
        return ResultUtils.success(result);
    }

    @PostMapping("/deleteComment")
    public BaseResponse<Boolean> deleteComment(@RequestParam("id") long commentId, HttpServletRequest request) {
        if (commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 不是本人的评论不能删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = currentUser.getId();
        boolean isMyComment = commentService.isMyComent(commentId, userId);
        if (!isMyComment) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        boolean c = commentService.removeById(commentId);
        return ResultUtils.success(c);
    }

    @GetMapping("/getLikesCount")
    public Long getLikesCount(@RequestParam("id") Long blogId) {
        long count = 1;     // 还没写
        return count;
    }

    @PostMapping("/addLikes")
    public BaseResponse<Long> addLikes(@RequestParam("id") long blogId, HttpServletRequest request){
        // 还没写
        return ResultUtils.success(blogId);
    }

    @PostMapping("/cancelLikes")
    public BaseResponse<Boolean> cancelLikes(@RequestParam("id") long blogId, HttpServletRequest request) {
        // 还没写
        return ResultUtils.success(true);
    }
}
