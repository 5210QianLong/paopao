-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    username      varchar(256)                        null comment '昵称',
    user_account  varchar(256)                        null comment '用户账号',
    profile       varchar(512)                        not null comment '用户简介',
    avatar_url    varchar(1024)                       null comment '头像',
    gender        tinyint                             null comment '性别 0-nan 1-nv',
    user_password varchar(512)                        not null comment '密码',
    phone         varchar(128)                        null comment '电话',
    email         varchar(512)                        null comment '邮箱',
    user_status   int       default 0                 null comment '账户状态',
    create_time   timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    id_detele     tinyint   default 0                 not null comment '逻辑删除',
    user_role     int       default 0                 not null comment '用户角色 0-普通 1 -管理',
    planet_code   varchar(512)                        null comment '星球编号',
    tags          varchar(1024)                       null comment '标签列表'
)
    comment '用户';

-- auto-generated definition
create table tag
(
    id          bigint auto_increment comment '用户id'
        primary key,
    tag_name    varchar(256)                        null comment '标签名称',
    user_id     bigint                              null comment '用户id',
    parent_id   bigint                              null comment '父标签id',
    is_parent   tinyint                             null comment '0-不是；1-是-父标签',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    id_detele   tinyint   default 0                 not null comment '逻辑删除'
)
    comment '标签';

create index unitag_name
    on tag (tag_name);

create index uniuser_id
    on tag (user_id);

alter table user add column tags varchar(1024) null comment '标签列表';

alter table tag convert to character set utf8;