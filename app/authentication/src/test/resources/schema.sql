CREATE TABLE users
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    username           VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE KEY,
    password           VARCHAR(255) NOT NULL,
    status             tinyint(1)   NOT NULL DEFAULT 1,
    create_date        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX index_name (email)
);