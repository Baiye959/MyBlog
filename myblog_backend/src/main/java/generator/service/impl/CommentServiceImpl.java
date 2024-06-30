package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Comment;
import generator.service.CommentService;
import generator.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 19389
* @description 针对表【comment(评论)】的数据库操作Service实现
* @createDate 2024-06-30 08:52:01
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




