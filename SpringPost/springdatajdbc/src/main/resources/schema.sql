DROP TABLE IF EXISTS CHESSGAME;

CREATE TABLE CHESSGAME
(
    id     BIGINT auto_increment,
    name   varchar(255),
    active bit default 1,
    primary key (id)
);

CREATE TABLE PIECES
(
    id       BIGINT auto_increment,
    game_id  BIGINT not null,
    piece    varchar(100),
    position varchar(100),
    primary key (id)
);

ALTER TABLE PIECES
    ADD FOREIGN KEY (game_id)
        REFERENCES CHESSGAME (id);

DROP TABLE IF EXISTS SUB_ONE;
DROP TABLE IF EXISTS SUPER_ONE;

CREATE TABLE SUPER_ONE
(
    id         BIGINT auto_increment,
    super_name varchar(255),
    primary key (id)
);

CREATE TABLE SUB_ONE
(
    id        BIGINT auto_increment,
    super_one BIGINT,
    sub_name  varchar(255),
    primary key (id)
);

ALTER TABLE SUB_ONE
    ADD FOREIGN KEY (super_one)
        REFERENCES SUB_ONE (id);

DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER
(
    id         BIGINT auto_increment,
    first_name varchar(255),
    last_name  varchar(255),
    primary key (id)
);

DROP TABLE IF EXISTS ENUM_ENTITY;

CREATE TABLE ENUM_ENTITY
(
    id     BIGINT auto_increment,
    active varchar(255),
    primary key (id)
);

DROP TABLE IF EXISTS SET_SINGLE;
DROP TABLE IF EXISTS SET_MANY;

CREATE TABLE SET_SINGLE
(
    id BIGINT auto_increment,
    primary key (id)
);

CREATE TABLE SET_MANY
(
    id         BIGINT auto_increment,
    set_single BIGINT,
    many_name  varchar(255),
    primary key (id)
);

ALTER TABLE SET_MANY
    ADD FOREIGN KEY (set_single)
        REFERENCES SET_SINGLE (id);

DROP TABLE IF EXISTS MAP_SINGLE;
DROP TABLE IF EXISTS MAP_MANY;

CREATE TABLE MAP_SINGLE
(
    id BIGINT auto_increment,
    primary key (id)
);

CREATE TABLE MAP_MANY
(
    id             BIGINT auto_increment,
    map_single     BIGINT,
    map_single_key varchar(255),
    content        varchar(255),
    primary key (id)
);

ALTER TABLE MAP_MANY
    ADD FOREIGN KEY (map_single)
        REFERENCES MAP_SINGLE (id);

DROP TABLE IF EXISTS LIST_SINGLE;
DROP TABLE IF EXISTS LIST_MANY;

CREATE TABLE LIST_SINGLE
(
    id BIGINT auto_increment,
    primary key (id)
);

CREATE TABLE LIST_MANY
(
    id              BIGINT auto_increment,
    list_single     BIGINT,
    list_single_key varchar(255),
    content         varchar(255),
    primary key (id)
);

ALTER TABLE LIST_MANY
    ADD FOREIGN KEY (list_single)
        REFERENCES LIST_SINGLE (id);

DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS ARTICLE;

CREATE TABLE ARTICLE
(
    id BIGINT auto_increment,
    primary key (id)
);

CREATE TABLE COMMENT
(
    id         BIGINT auto_increment,
    article_id BIGINT,
    article_key BIGINT,
    content    varchar(255),
    primary key (id)
);

ALTER TABLE COMMENT
    ADD FOREIGN KEY (article_id)
        REFERENCES ARTICLE (id);