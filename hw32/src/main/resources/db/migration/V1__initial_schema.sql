create table client
(
    id   bigserial primary key,
    name varchar(50) not null
);

create table phone
(
    id        bigserial primary key,
    number    varchar(50),
    client_id bigint not null references client (id)
);

create table address
(
    id        bigserial primary key,
    street    varchar(50),
    client_id bigint not null references client (id)
);

insert into client (name)
values ('Client 1'),
       ('Client 2'),
       ('Client 3');

insert into phone (number, client_id)
values ('Phone number 1', 1),
       ('Phone number 2 - 1', 2),
       ('Phone number 2 - 2', 2),
       ('Phone number 3', 3);

insert into address (street, client_id)
values ('Street 1', 1),
       ('Street 2', 2),
       ('Street 3', 3);