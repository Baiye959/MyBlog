package com.baiye959.myblog_backend.controller;

import com.baiye959.myblog_backend.common.BaseResponse;
import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.common.ResultUtils;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.model.domain.Blog;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.request.BlogRequest;
import com.baiye959.myblog_backend.service.BlogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.baiye959.myblog_backend.contant.UserContant.ADMIN_ROLE;
import static com.baiye959.myblog_backend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 博客接口
 *
 * @author zzzduo
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping("/getMyBlog")
    public BaseResponse<List<Blog>> getMyBlog(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = currentUser.getId();
        List<Blog> blogList=blogService.getMyBlog(userId);
        return ResultUtils.success(blogList);
    }

    @GetMapping("/getOneBlog")
    public BaseResponse<Blog> getOneBlog(@RequestParam("id") Long id) {
        Blog blog=blogService.getOneBlog(id);
        return ResultUtils.success(blog);
    }

    @GetMapping("/getAllBlog")
    public BaseResponse<List<Blog>> getAllBlog(HttpServletRequest request) {
        // 仅管理员可查看
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<Blog> blogList=blogService.getAllBlog();
        return ResultUtils.success(blogList);
    }

    @GetMapping("/getAllBlog2")
    public BaseResponse<List<Blog>> getAllBlog2(HttpServletRequest request) {
        List<Blog> blogList=blogService.getAllBlog();
        return ResultUtils.success(blogList);
    }

    @PostMapping("/addBlog")
    public BaseResponse<Long> addABlog(@RequestBody BlogRequest blogRequest, HttpServletRequest request){
        if (blogRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = currentUser.getId();
        String title = blogRequest.getTitle();
        String content = blogRequest.getContent();
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = blogService.addBlog(userId, title, content, time1, time2);
        return ResultUtils.success(result);
    }

    @PostMapping("/updateBlog")
    public BaseResponse<Long> updateABlog(@RequestBody BlogRequest blogRequest, HttpServletRequest request){
        if (blogRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = blogRequest.getId();
        String title = blogRequest.getTitle();
        String content = blogRequest.getContent();
        LocalDateTime time2 = LocalDateTime.now();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = blogService.updateBlog(id, title, content, time2);
        return ResultUtils.success(result);
    }

    @PostMapping("/deleteMyBlog")
    public BaseResponse<Boolean> deleteMyBlog(@RequestBody Blog blog) {
        Long id = blog.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = blogService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/deleteOtherBlog")
    public BaseResponse<Boolean> deleteOtherBlog(@RequestBody Blog blog, HttpServletRequest request) {
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long id = blog.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = blogService.removeById(id);
        return ResultUtils.success(b);
    }
    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
