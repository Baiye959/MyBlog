create database MyBlog;

create table user
(
    id bigint auto_increment primary key comment 'id',
    username varchar(256) not null comment '用户昵称',
    avatarUrl varchar(1024) null comment '用户头像',
    userPassword varchar(512) not null comment '密码',
    email varchar(512) not null comment '邮箱',
    userStatus int default 0 not null comment '状态 0 - 正常',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete tinyint default 0 not null comment '是否删除',
    userRole int default 0 not null comment '用户角色 0-默认用户 1-管理员'
) comment '用户';

create table blog(
     id bigint auto_increment primary key comment 'id',
     userId bigint comment '博客作者',
     title varchar(100) null comment '博客标题',
     content varchar(10000) null comment '博客内容',
     createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
     updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
     FOREIGN KEY (userId) REFERENCES user(id) ON DELETE SET NULL
) comment '博客';

CREATE TABLE `comment` (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY comment 'id',
    content varchar(255) DEFAULT NULL comment '评论内容',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    parentCommentId bigint DEFAULT NULL comment '被评论的评论',
    userId bigint comment '评论者',
    blogId bigint comment '被评论文章',
    FOREIGN KEY (parentCommentId) REFERENCES comment(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (blogId) REFERENCES blog(id) ON DELETE CASCADE
) comment '评论';

create table likes(
    userId bigint comment '点赞者',
    blogId bigint comment '被点赞文章',
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (blogId) REFERENCES blog(id) ON DELETE CASCADE,
    PRIMARY KEY(userId, blogId)
) comment '点赞';

create table announcement(
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY comment 'id',
    userId bigint comment '评论者',
    content varchar(10000) null comment '公告内容',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE SET NULL
) comment '公告';
