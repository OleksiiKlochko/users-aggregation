CREATE TABLE users
(
    user_id    VARCHAR(255) PRIMARY KEY,
    login      VARCHAR(255),
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

INSERT INTO users
VALUES ('example-user-id-1', 'user-1', 'User', 'Userenko');
