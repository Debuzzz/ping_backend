CREATE TABLE users (
    id UUID PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    avatar VARCHAR(255)
);

CREATE TABLE projects (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id UUID,
    FOREIGN KEY (owner_id) REFERENCES users(id)
); 