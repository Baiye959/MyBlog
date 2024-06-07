package com.baiye959.myblog_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName Annoucement
 */
@TableName(value ="announcement")
@Data
public class Announcement implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告发布者
     */
    private Long userId;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告创建时间
     */
    private Date createTime;

    /**
     * 公告修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
