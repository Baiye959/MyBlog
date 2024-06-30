package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 被评论的评论
     */
    private Long parentcommentid;

    /**
     * 评论者
     */
    private Long userid;

    /**
     * 被评论文章
     */
    private Long blogid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}