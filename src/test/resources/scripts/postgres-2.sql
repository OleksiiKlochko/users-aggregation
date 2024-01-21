CREATE TABLE user_table
(
    ldap_login VARCHAR(255) PRIMARY KEY,
    name       VARCHAR(255),
    surname    VARCHAR(255)
);

INSERT INTO user_table
VALUES ('example-user-id-2', 'Testuser', 'Testov');
