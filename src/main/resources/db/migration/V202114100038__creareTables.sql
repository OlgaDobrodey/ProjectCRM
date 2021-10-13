CREATE SCHEMA IF NOT EXISTS CRM;

CREATE TABLE IF NOT EXISTS crm.roles
(
    id        INT         NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id),
    CONSTRAINT role_unique UNIQUE (role_name)
);

CREATE TABLE IF NOT EXISTS crm.status
(
    id          INT         NOT NULL,
    status_name VARCHAR(50) NOT NULL,
    CONSTRAINT status_pkey PRIMARY KEY (id),
    CONSTRAINT status_unique UNIQUE (status_name)
);

CREATE TABLE IF NOT EXISTS crm.users
(
    id         INT         NOT NULL,
    login      VARCHAR(50) NOT NULL,
    psw        INT         NOT NULL,
    role       INT         NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login),
    CONSTRAINT fk_role FOREIGN KEY (role)
        REFERENCES roles (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS crm.tasks
(
    id      INT          NOT NULL,
    title   VARCHAR(250) NOT NULL,
    status  INT          NOT NULL,
    dedline DATE         NOT NULL,
    info    VARCHAR(250),
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT name_unique UNIQUE (title),
    CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES status (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION

);

CREATE TABLE IF NOT EXISTS crm.users_tasks
(
    user_id INT NOT NULL,
    task_id INT NOT NULL,
    info    VARCHAR(250),
    CONSTRAINT users_tasks_pkey PRIMARY KEY (user_id, task_id),
    CONSTRAINT fk_task_user FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_task FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO crm.status(id, status_name)
VALUES (1, 'new'),
       (2, 'performed'),
       (3, 'completed');