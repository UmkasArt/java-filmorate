delete
from FILM_CATEGORY;

delete
from LIKES;

delete
from FILMS;

delete
from MPA;

delete
from FRIENDS;

delete
from USERS;


insert into MPA
    (MPA_ID, NAME)
values (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

delete
from CATEGORY;

insert into CATEGORY
    (CATEGORY_ID, NAME)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');
//select U.* from FRIENDS f1 inner join FRIENDS f2 on f1.FRIEND_ID = f2.FRIEND_ID inner join USERS U on f1.FRIEND_ID = U.ID where f1.USER_ID = id and f2.USER_ID = otherId


/*select id,
       FILMS.name            as film_name,
       description,
       release_date,
       duration,
       FILMS.MPA_ID,
       RATE,
       M.NAME                AS mpa_name,
       count(USER_ID) + RATE as CNT
from FILMS
         left join LIKES L on FILMS.ID = L.FILM_ID
         inner join MPA M on FILMS.MPA_ID = M.MPA_ID
group by id, FILMS.name, description, release_date, duration, FILMS.MPA_ID, RATE
order by CNT
limit 10;*/

/*
SELECT FILMS.*, MPA.NAME AS MPA_NAME, c.CATEGORY_ID as GENRE_ID, c.NAME AS GENRE_NAME
FROM FILMS
         left join MPA on FILMS.MPA_ID = MPA.MPA_ID
inner join FILM_CATEGORY FC on FILMS.ID = FC.FILM_ID
inner join CATEGORY C on C.CATEGORY_ID = FC.CATEGORY_ID;*/

/*
select c.CATEGORY_ID as GENRE_ID, C.NAME AS GENRE_NAME
from FILM_CATEGORY
         inner join CATEGORY C on C.CATEGORY_ID = FILM_CATEGORY.CATEGORY_ID
WHERE FILM_CATEGORY.FILM_ID = 2
ORDER BY GENRE_ID;*/
