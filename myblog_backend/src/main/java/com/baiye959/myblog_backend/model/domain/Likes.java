package com.baiye959.myblog_backend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 点赞
 * @TableName likes
 */
@TableName(value ="likes")
@Data
public class Likes implements Serializable {
    /**
     * 点赞者
     */
    private Long userId;

    /**
     * 被点赞文章
     */
    private Long blogId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
