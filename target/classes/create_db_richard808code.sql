CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(60) NOT NULL
);

-- Resetování auto-incrementu kvůli snadnějšímu testování

ALTER TABLE users AUTO_INCREMENT = 1;