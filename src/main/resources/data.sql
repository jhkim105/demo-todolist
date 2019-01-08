-- Task
INSERT INTO tu_task (id, created_at, description, closed, updated_at) VALUES (1, '2019-01-04 13:02:03', '집안일', 0, '2019-01-05 15:00:31');
INSERT INTO tu_task (id, created_at, description, closed, updated_at) VALUES (2, '2019-01-04 13:03:03', '빨래', 0, '2019-01-06 13:02:03');
INSERT INTO tu_task (id, created_at, description, closed, updated_at) VALUES (3, '2019-01-04 14:01:03', '청소', 0, '2019-01-04 14:02:03');
INSERT INTO tu_task (id, created_at, description, closed, updated_at) VALUES (4, '2019-01-04 14:12:03', '방청소', 0, '2019-01-05 14:02:03');
INSERT INTO tu_related_task (task_id, super_task_id) VALUES (2, 1);
INSERT INTO tu_related_task (task_id, super_task_id) VALUES (3, 1);
INSERT INTO tu_related_task (task_id, super_task_id) VALUES (4, 1);
INSERT INTO tu_related_task (task_id, super_task_id) VALUES (4, 3);
-- User
insert into tu_user (id, username, password) values(1, 'user', 'user');
insert into tu_user_authorities(user_id, authority) values (1, 'USER');
