create table if not exists mpa
(
    mpa_id int      not null,
    name   char(10) not null
);

create unique index if not exists MPA_MPA_ID_UINDEX
    on mpa (mpa_id);

alter table mpa
    add constraint if not exists MPA_PK
        primary key (mpa_id);



create table if not exists category
(
    category_id int      not null,
    name        varchar2 not null,
    constraint CATEGORY_PK
        primary key (category_id)
);

create table if not exists films
(
    id           int      not null,
    name         varchar2 not null,
    description  varchar2,
    release_date date,
    duration     int,
    rate         int,
    mpa_id       int,
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (mpa_id) references mpa (mpa_id)
);

create unique index if not exists FILMS_ID_UINDEX
    on films (id);

alter table films
    add constraint if not exists FILMS_PK
        primary key (id);

create table if not exists film_category
(
    film_id     int,
    category_id int,
    constraint FILM_CATEGORY_CATEGORY_CATEGORY_ID_FK
        foreign key (category_id) references category (category_id),
    constraint FILM_CATEGORY_FILM_ID_FK
        foreign key (film_id) references films (id)
);

create table if not exists users
(
    id       int      not null,
    login    varchar2 not null,
    name     varchar2,
    email    varchar2 not null,
    birthday date
);

create unique index if not exists USERS_ID_UINDEX
    on users (id);

alter table users
    add constraint if not exists USERS_PK
        primary key (id);

create table if not exists likes
(
    film_id int,
    user_id int,
    constraint LIKES_FILMS_ID_FK
        foreign key (film_id) references films (id),
    constraint LIKES_USERS_ID_FK
        foreign key (user_id) references users (id)
);

create table if not exists friends
(
    user_id   int,
    friend_id int,
    status    boolean,
    constraint FRIENDS_USERS_ID_FK
        foreign key (user_id) references USERS,
    constraint FRIENDS_USERS_ID_FK_2
        foreign key (friend_id) references USERS
);

/*drop table if exists film_category;
drop table if exists likes;
drop table if exists films;
drop table if exists rating;*/
