truncate table roles cascade;
select setval('roles_id_seq', 1, false);
truncate table users cascade;
select setval('users_id_seq', 1, false);
truncate table accounts cascade;
select setval('accounts_id_seq', 1, false);