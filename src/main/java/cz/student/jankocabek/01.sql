create table users
(
    id       int(11) auto_increment primary key,
    email    varchar(255) not null UNIQUE,
    username varchar(255) not null,
    password varchar(60)  not null
);

