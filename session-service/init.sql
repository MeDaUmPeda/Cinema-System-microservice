-- init.sql
DROP TABLE IF EXISTS sessions;

CREATE TABLE sessions (
                          id SERIAL PRIMARY KEY,
                          movie_id BIGINT NOT NULL,
                          available_seats INT NOT NULL,
                          date_time TIMESTAMP NOT NULL
);
