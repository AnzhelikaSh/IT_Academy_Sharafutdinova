---------СОЗДАНИЕ-ТАБЛИЦ-------------------
create sequence account_sequence
    start with 100000
    increment by 1
    cache 50;

create table account (
    id              bigint default nextval('account_sequence') not null,
    name            varchar(20) not null,
    -----------------------------------------------------------
    constraint account_pk primary key(id),
    constraint account_name_uq unique (name)
);
comment on table account                     is 'пользоваетль';
comment on column  account.id                is 'идентификатор пользователя';
comment on column  account.name              is 'имя пользователя';

create sequence music_sequence
    start with 100000
    increment by 1
    cache 50;

create table music (
    id              bigint default nextval('music_sequence') not null,
    song            varchar(20) not null,
    song_length     real not null,
    -----------------------------------------------------------
    constraint music_pk primary key(id)
);
comment on table music                     is 'музыка';
comment on column  music.id                is 'идентификатор музыки';
comment on column  music.song              is 'название песни';
comment on column  music.song_length       is 'длина песни';

create table account_music (
    account_id     bigint not null,
    music_id       bigint not null,
    ------------------------------------------------
    constraint account_music_pk           primary key (account_id, music_id),
    constraint account_music_account_fk   foreign key (account_id) references account(id),
    constraint account_music_music_fk     foreign key (music_id) references music(id)
);
comment on table   account_music               is 'пользователь-музыка';
comment on column  account_music.account_id    is 'идентификатор автора';
comment on column  account_music.music_id      is 'идентификатор песни';

create table playlist (
    account_id     bigint not null,
    music_id       bigint not null,
------------------------------------------------
    constraint playlist_pk              primary key (account_id, music_id),
    constraint playlist_account_fk      foreign key (account_id) references account(id),
    constraint playlist_music_fk        foreign key (music_id) references music(id)
);
comment on table   playlist               is 'плейлист';
comment on column  playlist.account_id    is 'идентификатор пользователя';
comment on column  playlist.music_id      is 'идентификатор песни';

-----------------ЗАПРОСЫ-------------
----добавляем пользователя/автора----
insert into account (name)
values ('Eminem');

----добавляем песню----
with new_song as (
    insert into music (song, song_length)
        values ('Believe', 3.12)
        returning id
)
insert into account_music (account_id, music_id)
select a.id, ns.id
from account a, new_song ns
where a.name = 'Elton John';

----добавляем песню с двумя авторами----
with new_song as (
    insert into music (song, song_length)
        values ('Monsters', 3.48)
        returning id
)
insert into account_music (account_id, music_id)
select a.id, ns.id
from account a
         join new_song ns on true
where a.name in ('Rihanna', 'Eminem');

----добавляем песню в плейлист пользователя----
insert into playlist (account_id, music_id)
values (
        (select id from account where name = 'Britney Spears'),
        (select id from music where song = 'Believe')
       );

----добавляем все песни автора (a.name) в плейлист пользователя(u.name)----
insert into playlist (account_id, music_id)
select
    u.id,
    am.music_id
from account_music am
join account a on a.id = am.account_id
join account u on u.name = 'Britney Spears'
where a.name = 'Eminem';



----выводим плейлист пользователя----
select
    string_agg(a.name, ', ') as authors,
    m.song,
    m.song_length
from playlist p
         join account u on u.id = p.account_id
         join music m on m.id = p.music_id
         join account_music am on am.music_id = m.id
         join account a on a.id = am.account_id
where u.name = 'Britney Spears'
group by a.name, m.song, m.song_length;


----выводим все песни автора----
select
    m.song,
    m.song_length
from account_music am
         join account a on a.id = am.account_id
         join music m on m.id = am.music_id
where a.name = 'Eminem'
group by a.name, m.song, m.song_length;