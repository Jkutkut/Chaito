-- Drop previous tables if they exist
DROP TABLE IF EXISTS MSGS;

-- Create the table
CREATE TABLE MSGS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    target_user VARCHAR(80) NOT NULL,
    sender_user VARCHAR(80) NOT NULL,
    msg VARCHAR(500) NOT NULL
);

-- Initial load

INSERT INTO MSGS (target_user, sender_user, msg) VALUES ('Mariadb', 'Jkutkut', 'Hi there, are we live?');
INSERT INTO MSGS (target_user, sender_user, msg) VALUES ('Jkutkut', 'Mariadb', 'Yes, we are live!');