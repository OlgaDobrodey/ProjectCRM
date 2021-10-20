CREATE TABLE IF NOT EXISTS role
(
    id        INT         NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (id),
    CONSTRAINT role_unique UNIQUE (role_name)
);

CREATE TABLE IF NOT EXISTS user
(
    id         INT          NOT NULL AUTO_INCREMENT,
    login      VARCHAR(50)  NOT NULL,
    psw        VARCHAR(255) NOT NULL,
    role       INT          NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login),
    CONSTRAINT fk_role FOREIGN KEY (role)
        REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS task
(
    id       INT          NOT NULL AUTO_INCREMENT,
    title    VARCHAR(250) NOT NULL,
    status   VARCHAR(8) NOT NULL,
    deadline DATE,
    info     VARCHAR(250),
    CONSTRAINT task_pkey PRIMARY KEY (id),
    CONSTRAINT name_unique UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS user_task
(
    user_id INT NOT NULL,
    task_id INT NOT NULL,
    CONSTRAINT user_task_pkey PRIMARY KEY (user_id, task_id),
    CONSTRAINT fk_task_user FOREIGN KEY (task_id)
        REFERENCES task (id),
    CONSTRAINT fk_user_task FOREIGN KEY (user_id)
        REFERENCES user (id)
);