truncate table roles cascade;
select setval('roles_id_seq', 1, false);
insert into roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');
