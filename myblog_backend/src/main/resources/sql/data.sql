USE MyBlog;

# 插入初始数据
INSERT INTO user (username, avatarUrl, userPassword, email, userStatus, userRole)
VALUES
    ('test1', 'https://media.prts.wiki/thumb/f/fe/Avatar_def_06.png/80px-Avatar_def_06.png', 'password123', 'test1@example.com', 0, 0),
    ('test2', 'https://media.prts.wiki/thumb/3/34/Avatar_def_03.png/80px-Avatar_def_03.png', 'password456', 'test2@example.com', 0, 0),
    ('Admin User', 'https://media.prts.wiki/thumb/4/4f/Avatar_def_14.png/80px-Avatar_def_14.png', 'admin123', 'admin@example.com', 0, 1);

INSERT INTO blog (userId, title, content)
VALUES
    (1, 'My First Blog Post', 'This is the content of my first blog post.'),
    (2, 'Exploring the City', 'In this blog post, I share my experiences exploring the city.'),
    (1, 'Tips for Productivity', 'Here are some tips I have learned to boost my productivity.');

INSERT INTO comment (content, userId, blogId)
VALUES
    ('Great post!', 2, 1),
    ('I really enjoyed reading this.', 3, 2),
    ('Useful tips, thanks for sharing!', 1, 3);

INSERT INTO likes (userId, blogId)
VALUES
    (1, 2),
    (2, 1),
    (3, 3);

INSERT INTO announcement (userId, content)
VALUES
    (3, 'welcome!');

INSERT INTO photo (photoUrl, userId)
VALUES
    ('https://media.prts.wiki/thumb/3/34/Avatar_def_03.png/80px-Avatar_def_03.png', 1);