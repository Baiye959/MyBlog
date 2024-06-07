package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Announcement;
import generator.service.AnnouncementService;
import generator.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

/**
* @author 19389
* @description 针对表【announcement(公告)】的数据库操作Service实现
* @createDate 2024-06-05 10:42:49
*/
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService{

}




