INSERT INTO role(id, role_name)
VALUES (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'CONTROLLER'),
       (4, 'TEST');


INSERT INTO user(id, login, psw, role_id, last_name, first_name)
VALUES (1, 'Petrov', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 1, 'Petrov', 'Petr'),
       (2, 'Ivanov','$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 1, 'Ivanov', 'Ivan'),
       (3, 'Sidorov','$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 1, 'Sidorov', 'Sidr'),
       (4, 'Frolov','$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 3, 'Frolov', 'Vova'),
       (5, 'Kozlov', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 2, 'Kozlov', 'Igor'),
       (6, 'Kolpokova', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 2, 'Kolpokova', 'Sveta'),
       (7, 'Kulak', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 3, 'Kulak', 'Tanyay'),
       (8, 'Dropalo', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 2, 'Dropalo', 'Andrey'),
       (9, 'Kurgan', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 3, 'Kurgan', 'Misha'),
       (10, 'Leurdo', '$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS', 2, 'Leurdo', 'Elena');

INSERT INTO task(id, title, status, deadline, info)
VALUES (1, 'task_title 1', 'NEW', '2022-10-23', 'task info 1'),
       (2, 'task title 2', 'NEW', '2022-12-23', 'task info 2'),
       (3, 'task title 3', 'PROGRESS', '2021-12-23', 'task info 3'),
       (4, 'task title 4', 'NEW', '2022-10-23', 'task info 4'),
       (5, 'task title 5', 'PROGRESS', '2019-10-23', 'task info 5'),
       (6, 'task title 6', 'DONE', '2018-10-23', null),
       (7, 'task title 7', 'PROGRESS', '2022-10-23', 'task info 7'),
       (8, 'task title 8', 'PROGRESS', '2022-03-15', 'task info 8'),
       (9, 'task title 9', 'NEW', '2022-08-23', null),
       (10, 'task title 10', 'PROGRESS', '2022-08-16', 'task info 10'),
       (11, 'task title 11', 'DONE', '2012-10-23', 'task info 11'),
       (12, 'task title 12', 'PROGRESS', '2032-10-23', 'task info 12'),
       (13, 'task title 13', 'DONE', '2019-05-23', 'task info 13'),
       (14, 'task title 14', 'NEW', '2024-12-25', 'task info 14'),
       (15, 'task title 15', 'NEW', '2022-10-14', 'task info 15'),
       (16, 'task title 16', 'PROGRESS', '2023-04-19', 'task info 16'),
       (17, 'task title 17', 'PROGRESS', '2022-05-21', 'task info 17'),
       (18, 'task title 18', 'NEW', '2022-07-29', 'task info 18'),
       (19, 'task title 19', 'NEW', '2025-12-30', 'task info 19'),
       (20, 'task title 20', 'DONE', '2020-11-28', 'task info 20');

INSERT INTO user_task(users_id, tasks_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (4, 7),
       (4, 8),
       (4, 9),
       (4, 10),
       (5, 11),
       (5, 12),
       (6, 13),
       (6, 14),
       (6, 15),
       (6, 16),
       (7, 17),
       (7, 18),
       (7, 19),
       (7, 3),
       (8, 2),
       (8, 3),
       (9, 4),
       (9, 5),
       (9, 6),
       (10, 7),
       (10, 8),
       (10, 9),
       (1, 10),
       (2, 11),
       (3, 12),
       (4, 13),
       (5, 14),
       (6, 12),
       (7, 16),
       (8, 17),
       (9, 18),
       (10, 19),
       (1, 19);

COMMIT