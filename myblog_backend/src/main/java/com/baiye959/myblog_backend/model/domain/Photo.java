package com.baiye959.myblog_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName photo
 */
@TableName(value ="photo")
@Data
public class Photo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String photoUrl;

    /**
     * 
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}