package com.baiye959.myblog_backend.service.impl;

import com.baiye959.myblog_backend.common.ErrorCode;
import com.baiye959.myblog_backend.exception.BusinessException;
import com.baiye959.myblog_backend.mapper.BlogMapper;
import com.baiye959.myblog_backend.model.domain.Blog;
import com.baiye959.myblog_backend.model.domain.User;
import com.baiye959.myblog_backend.service.BlogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    /**
     * 查询自己的博客
     *
     * @param userId
     * @return
     */
    @Override
    public List<Blog> getMyBlog(long userId) {

        // 创建 LambdaQueryWrapper 对象
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        wrapper.eq(Blog::getUserId, userId);

        List<Blog> blogs = blogMapper.selectList(wrapper);
        return blogs;
    }

    /**
     * 查询特定博客
     * @param id
     * @return
     */
    @Override
    public Blog getOneBlog(Long id){
        // 创建 LambdaQueryWrapper 对象
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        wrapper.eq(Blog::getId, id);

        Blog blog = blogMapper.selectOne(wrapper);
        return blog;
    }

    /**
     * 管理员查询所有博客
     * @return
     */
    @Override
    public List<Blog> getAllBlog() {
        List<Blog> blogs = blogMapper.selectList(null);
        return blogs;
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
