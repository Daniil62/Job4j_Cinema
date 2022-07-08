CREATE TABLE users (
id SERIAL PRIMARY KEY,
username VARCHAR NOT NULL,
email VARCHAR NOT NULL UNIQUE,
phone VARCHAR NOT NULL UNIQUE,
password VARCHAR
);

CREATE TABLE sessions (
id SERIAL PRIMARY KEY,
name VARCHAR NOT NULL UNIQUE,
poster bytea
);

CREATE TABLE tickets (
id SERIAL PRIMARY KEY,
session_id INT NOT NULL REFERENCES sessions(id),
pos_row INT NOT NULL,
cell INT NOT NULL,
user_id INT NOT NULL REFERENCES users(id),
UNIQUE (session_id, pos_row, cell)
);