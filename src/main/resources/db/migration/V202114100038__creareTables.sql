CREATE TABLE IF NOT EXISTS role
(
    id        INT         NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (id),
    CONSTRAINT role_unique UNIQUE (role_name)
);

CREATE TABLE IF NOT EXISTS status
(
    id          INT         NOT NULL AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL,
    CONSTRAINT status_pkey PRIMARY KEY (id),
    CONSTRAINT status_unique UNIQUE (status_name)
);

CREATE TABLE IF NOT EXISTS user
(
    id         INT         NOT NULL AUTO_INCREMENT,
    login      VARCHAR(50) NOT NULL,
    psw        INT         NOT NULL,
    role       INT,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login),
    CONSTRAINT fk_role FOREIGN KEY (role)
        REFERENCES role (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS task
(
    id      INT          NOT NULL AUTO_INCREMENT,
    title   VARCHAR(250) NOT NULL,
    status  INT,
    dedline DATE,
    info    VARCHAR(250),
    CONSTRAINT task_pkey PRIMARY KEY (id),
    CONSTRAINT name_unique UNIQUE (title),
    CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES status (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_task
(
    user_id INT NOT NULL,
    task_id INT NOT NULL,
    info    VARCHAR(250),
    CONSTRAINT user_task_pkey PRIMARY KEY (user_id, task_id),
    CONSTRAINT fk_task_user FOREIGN KEY (task_id)
        REFERENCES task (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_user_task FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);