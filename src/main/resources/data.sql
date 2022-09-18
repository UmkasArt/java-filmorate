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



/*select * from USERS;
select * from FILMS;*/


/*select * from FILMS
where id in (select FILM_ID, count(USER_ID) as CNT from LIKES
             group by FILM_ID
             order by CNT desc
             limit 10);*/


/*select id, name, description, release_date, duration, RATE, MPA_ID, count(USER_ID) + RATE as CNT
from FILMS inner join LIKES L on FILMS.ID = L.FILM_ID group by id order by CNT limit 10;*/


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
limit 10;


select *
from FILMS;
select *
from LIKES
where FILM_ID = 1;

SELECT FILMS.*, MPA.NAME AS MPA_NAME
FROM FILMS
         left join MPA on FILMS.MPA_ID = MPA.MPA_ID;

select *
from FILM_CATEGORY;*/