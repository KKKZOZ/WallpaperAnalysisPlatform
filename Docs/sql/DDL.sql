create table wallpaper_info(
    wallpaper_id bigserial,
    name varchar(255),
    url varchar(255),
    type varchar(255),
    category varchar(255),
    rating int,
    resolution varchar(255),
    unique_visitors int,
    current_subscribers int,
    current_favorites int,
    create_time timestamp,

    primary key (wallpaper_id)
);


create table wallpaper_comment(
    comment_id bigserial,
    wallpaper_id bigint,
    username varchar(255),
    avatar_url varchar(255),
    create_time timestamp,
    content varchar(255),

    primary key (comment_id)
);

create table platform_user(
    user_id bigserial,
    username varchar(255),
    password varchar(255),
    email varchar(255),
    avatar_url varchar(255),
    is_enabled boolean,
    primary key (user_id)
);

create table wallpaper_set(
    id bigserial,
    wallpaper_id bigint,
    set_id bigint,

    primary key (id)
);

create table set(
    set_id bigserial,
    set_name varchar(255),
    user_id bigint,
    create_time timestamp,
    is_public boolean,
    like_count int,
    dislike_count int,
    cover_url varchar(255),
    primary key (set_id)
);

create table user_comment(
    comment_id bigserial,
    set_id bigint,
    article_id bigint,
    publisher_id bigint,
    publish_time timestamp,
    content varchar(2550),
    like_count int,
    dislike_count int,
    primary key (comment_id)
);

create table article(
    article_id bigserial,
    publisher_id bigint,
    title varchar(255),
    content varchar(2550),
    publish_time timestamp,
    like_count int,
    dislike_count int,
    primary key (article_id)
);

create table like_status(
    status_id bigserial,
    user_id bigint,
    article_id bigint,
    set_id bigint,
    comment_id bigint,
    status int,

    primary key (status_id)
);









