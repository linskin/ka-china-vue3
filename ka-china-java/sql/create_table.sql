CREATE DATABASE IF NOT EXISTS ka_china;

USE ka_china;

# -- 用户表
CREATE TABLE IF NOT EXISTS user
(
    id         BIGINT COMMENT 'id' PRIMARY KEY,
    name       VARCHAR(256) COLLATE utf8mb4_unicode_ci                NOT NULL COMMENT '姓名',
    account    VARCHAR(256) COLLATE utf8mb4_unicode_ci                NOT NULL COMMENT '手机号',
    password   VARCHAR(512) COLLATE utf8mb4_unicode_ci                NOT NULL COMMENT '密码',
    avatar     VARCHAR(1024) COLLATE utf8mb4_unicode_ci               NULL COMMENT '头像',
    role       VARCHAR(256) DEFAULT 'user' COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户角色：user/ admin/ super_admin',
    createTime DATETIME     DEFAULT CURRENT_TIMESTAMP                 NOT NULL COMMENT '创建时间',
    updateTime DATETIME     DEFAULT CURRENT_TIMESTAMP                 NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT uni_account UNIQUE (account)
) COMMENT '用户' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_user_name ON user (name);
CREATE INDEX idx_user_account ON user (account);
INSERT INTO user VALUES (1668104926409170946, '管理员', 'admin', 'e340043f1a20bc4c0f05d8b14459a1fa', NULL, 'super_admin', '2023-06-12 11:56:21', '2023-06-12 11:57:19');

# -- 操作日志表
create table if not exists operation_log
(
    id             bigint auto_increment comment 'id' primary key,
    requestId      varchar(256) default '' comment '请求唯一ID',
    title          varchar(256) default '' comment '业务标题',
    requestMethod  varchar(256) default '' comment '请求方式',
    operationUrl   varchar(256) default '' comment '请求URL',
    userId         bigint       default 0 not null comment '操作用户',
    operationIp    varchar(256) default '' comment '主机地址',
    operationParam text comment '请求参数',
    result         text comment '返回参数',
    operationTime  datetime comment '操作时间',
    costTime       bigint(20)   default 0 comment '消耗时间'
) comment '操作日志记录' charset = utf8mb4
                         collate = utf8mb4_unicode_ci;
create index idx_operation_title on operation_log (title);
create index idx_operation_method on operation_log (requestMethod);
create index idx_operation_time on operation_log (operationTime);

# -- 文件表
CREATE TABLE IF NOT EXISTS files
(
    id          BIGINT COMMENT 'id' PRIMARY KEY,
    fileName    VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '文件名',
    fileType    VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '类型',
    fileSize    VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '文件大小',
    fileUuid    VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '文件UUID',
    fileMd5     VARCHAR(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'md5',
    downloadUrl VARCHAR(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '下载链接',
    uploaderId  BIGINT                                                         NOT NULL COMMENT '上传者',
    createTime  DATETIME                                                            DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime  DATETIME                                                            DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) COMMENT '文件' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_file_name ON files (fileName);
CREATE INDEX idx_file_uuid ON files (fileUuid);
CREATE INDEX idx_file_type ON files (fileType);

# -- banner表
CREATE TABLE IF NOT EXISTS banner
(
    id         BIGINT COMMENT 'id' PRIMARY KEY,
    name       VARCHAR(256) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '名称',
    cover      VARCHAR(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面',
    userId     bigint                                   NOT NULL COMMENT '创建用户',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT 'banner' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_banner_name ON banner (name);

# -- 分类表
CREATE TABLE IF NOT EXISTS category
(
    id         BIGINT COMMENT 'id' PRIMARY KEY,
    name       VARCHAR(256) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '名称',
    userId     bigint                                   NOT NULL COMMENT '创建用户',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '分类' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_category_name ON category (name);

# -- 帖子表
CREATE TABLE IF NOT EXISTS post
(
    id          BIGINT COMMENT 'id' PRIMARY KEY,
    title       VARCHAR(256) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '标题',
    cover       VARCHAR(1024) COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
    description VARCHAR(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述',
    content     text COLLATE utf8mb4_unicode_ci          NOT NULL COMMENT '内容',
    userId      bigint                                   NOT NULL COMMENT '创建用户',
    createTime  DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL COMMENT '创建时间',
    updateTime  DATETIME DEFAULT CURRENT_TIMESTAMP       NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '帖子' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_post_name ON post (title);

-- 帖子_分类表
CREATE TABLE IF NOT EXISTS post_category
(
    id         BIGINT auto_increment COMMENT 'id' PRIMARY KEY,
    postId     BIGINT                             NOT NULL COMMENT '帖子ID',
    categoryId BIGINT                             NOT NULL COMMENT '分类ID',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (postId, categoryId)
) COMMENT '帖子_分类表' CHARSET = utf8mb4
                 COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_post_category_pid ON post_category (postId);
CREATE INDEX idx_post_category_cid ON post_category (categoryId);