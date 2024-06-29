package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.BlogMapper;
import com.baiye959.myblog_backend.mapper.UserMapper;
import com.baiye959.myblog_backend.model.domain.Blog;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.model.domain.response.BlogResponse;
import com.baiye959.myblog_backend.service.BlogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzzduo
 */

@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    private UserMapper userMapper;

    /**
     * 查询自己的博客
     *
     * @param userId
     * @return
     */
    @Override
    public List<BlogResponse> getMyBlog(long userId) {

        LambdaQueryWrapper<Blog> blogWrapper = new LambdaQueryWrapper<>();
        blogWrapper.eq(Blog::getUserId, userId);
        List<Blog> blogs = blogMapper.selectList(blogWrapper);

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(userWrapper);

        List<BlogResponse> blogList = new ArrayList<>();
        for(Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setUser(user);
            blogResponse.setId(blog.getId());
            blogResponse.setTitle(blog.getTitle());
            blogResponse.setContent(blog.getContent());
            blogResponse.setCreateTime(blog.getCreateTime());
            blogResponse.setUpdateTime(blog.getUpdateTime());
            blogList.add(blogResponse);
        }
        return blogList;
    }

    /**
     * 查询特定博客
     * @param id
     * @return
     */
    @Override
    public BlogResponse getOneBlog(Long id){

        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getId, id);
        Blog blog = blogMapper.selectOne(wrapper);

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getId, blog.getUserId());
        User user = userMapper.selectOne(userWrapper);

        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setId(blog.getId());
        blogResponse.setTitle(blog.getTitle());
        blogResponse.setContent(blog.getContent());
        blogResponse.setCreateTime(blog.getCreateTime());
        blogResponse.setUpdateTime(blog.getUpdateTime());
        blogResponse.setUser(user);

        return blogResponse;
    }

    /**
     * 管理员查询所有博客
     * @return
     */
    @Override
    public List<BlogResponse> getAllBlog() {
        List<Blog> blogs = blogMapper.selectList(null);
        List<BlogResponse> blogList = new ArrayList<>();

        for(Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setId(blog.getId());
            blogResponse.setTitle(blog.getTitle());
            blogResponse.setContent(blog.getContent());
            blogResponse.setCreateTime(blog.getCreateTime());
            blogResponse.setUpdateTime(blog.getUpdateTime());

            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getId, blog.getUserId());
            User user = userMapper.selectOne(userWrapper);
            blogResponse.setUser(user);

            blogList.add(blogResponse);
        }
        return blogList;
    }

    @Override
    @Transactional
    public Long addBlog(long userId, String title, String content, LocalDateTime time1, LocalDateTime time2) {

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setCreateTime(time1);
        blog.setUpdateTime(time2);
        boolean saveResult = this.save(blog);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据库失败");
        }
        return blog.getId();
    }

    @Override
    public Long updateBlog(long id, String title, String content, LocalDateTime time2){

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        Blog blog = blogMapper.selectOne(queryWrapper);

        blog.setTitle(title);
        blog.setContent(content);
        blog.setUpdateTime(time2);

        blogMapper.updateById(blog);

        // 返回更新后的博客 id
        return blog.getId();
    }

}
