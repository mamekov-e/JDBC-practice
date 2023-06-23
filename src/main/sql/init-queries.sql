-- create table role
-- (
--     id   serial      not null primary key,
--     name varchar(20) not null
-- );

create table users
(
    id       serial      not null primary key,
    username varchar(50) not null,
    password varchar(50) not null,
    role_name  varchar(20)not null
);
