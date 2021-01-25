SET SQL_SAFE_UPDATES = 0;

create table usersaccount (
id int unsigned auto_increment,
id_u int unsigned unique,
username varchar(50),
passwords varchar(50),
constraint pk_id primary key(id)
);

create table users (
id int unsigned auto_increment,
accountName varchar(50),
dateOfbirth varchar(20),
city varchar(50),
phone int unsigned,
email varchar(50),
constraint pk_id primary key (id)
);

create table song (
id int unsigned auto_increment,
sourcePath varchar(50),
constraint pk_id primary key (id)
);

create table songDetail (
id int unsigned auto_increment,
id_song int unsigned,
title varchar(50),
author varchar(50),
duration double,
imgUrl varchar(100),
constraint pk_id primary key(id)
);

delete from song;
select * from songDetail;
select * from song;
select * from recentplay;

drop table songdetail;

create table playlist (
id int unsigned auto_increment,
id_song int unsigned,
id_user int unsigned,
constraint pk_id primary key (id)
);

create table recentPlay (
id int unsigned auto_increment,
id_song int unsigned,
id_user int unsigned,
constraint pk_id primary key (id)
);


alter table usersaccount add constraint fk_id foreign key (id_u) references users(id) on delete cascade;
alter table playlist add constraint fk_song foreign key (id_song) references song(id) on delete cascade;
alter table Playlist add constraint fk_user foreign key (id_user) references users(id) on delete cascade;
alter table recentPlay add constraint fkr_song foreign key (id_song) references song(id) on delete cascade;
alter table recentPlay add constraint fkr_user foreign key (id_user) references users(id) on delete cascade;
alter table songDetail add constraint fks_song foreign key (id_song) references song(id) on delete cascade;

drop table song;
drop table playlist;

alter table usersaccount modify username varchar(50) unique;
select * from usersaccount;
select * from users;
select * from song;
select * from songDetail;
select * from playlist;
select * from recentplay;

select * from songDetail where title like 'nang';

-- delete data --
delete from recentplay where id_user = 6 order by id;
delete from playlist where id_user = 6 order by id;

-- tmp insert --
insert into recentplay (id_user, id_song) values ("6", "1");
insert into recentplay (id_user, id_song) values ("6", "2");

-- tmp insert --
insert into playlist (id_user, id_song) values ("6", "1");
insert into playlist (id_user, id_song) values ("6", "2");

-- handle with recent play --
SELECT * FROM recentplay ORDER BY id DESC LIMIT 5;
delete from recentplay where id_user = 6 order by id limit 1;
insert into recentplay (id_user, id_song) values ("6", "1");
SELECT COUNT(*) FROM recentplay where id_user = 6;
select * from recentplay where id_user = 6 order by id desc limit 6;

-- insert song --
insert into song (sourcePath) values ("music/nangtho.mp3");
insert into songDetail (id_song, title, author, duration) values ("1", "nangtho", "hoang dung", "4");
insert into song (sourcePath) values ("music/suytnuathi.mp3");
insert into songDetail (id_song, title, author, duration) values ("2", "suyt nua thi", "andiez", "4");

SELECT * FROM mysql.user;
UPDATE mysql.user SET Password=PASSWORD('ainsoft99') WHERE User='root';
alter user "root"@"localhost" identified by "ainsoft99";
