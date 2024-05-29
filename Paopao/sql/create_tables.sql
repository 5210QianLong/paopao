-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    username      varchar(256)                        null comment '昵称',
    user_account  varchar(256)                        null comment '用户账号',
    profile       varchar(512)                        null comment '用户简介',
    avatar_url    varchar(1024)                       null comment '头像',
    gender        tinyint                             null comment '性别 0-nan 1- nv',
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
    comment '用户' charset = utf8;

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
-- 队伍表
-- auto-generated definition
create table team
(
    id          bigint auto_increment comment '队伍id'
        primary key,
    team_name   varchar(256)                        null comment '队伍名称',
    description varchar(1024)                       null comment '队伍描述',
    max_num     int       default 1                 not null comment '最大人数',
    expire_time datetime                            not null comment '过期时间',
    user_id     bigint                              null comment '用户id',
    leader_id   bigint                              null comment '队长',
    status      int       default 0                 null comment '0-公开 1-私有 2-加密',
    password    varchar(512)                        null comment '队伍密码',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    id_delete   tinyint   default 0                 not null comment '逻辑删除'
)
    comment '队伍' charset = utf8;

-- 队伍用户关系表
create table user_team
(
    id            bigint auto_increment    comment '队关系表d' primary key,
    user_id       bigint                                   comment '用户id',
    team_id       bigint                                   comment '队伍id',
    join_time     datetime                               null comment '加入队伍时间',
    create_time   timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    id_delete     tinyint   default 0                 not null comment '逻辑删除'
)
    comment '队伍用户关系表' charset = utf8;