truncate table roles cascade;
select setval('roles_id_seq', 1, false);
truncate table users cascade;
select setval('users_id_seq', 1, false);
insert into roles (name)
       values ('ROLE_ADMIN'),
              ('ROLE_USER');
insert into users (username, password, role_id)
values ('user', 'password', 2),
       ('admin', 'password', 1);
truncate table accounts cascade;
select setval('accounts_id_seq', 1, false);
insert into accounts (balance, status, user_id)
values (10, true, 1),
       (20, false, 2);
