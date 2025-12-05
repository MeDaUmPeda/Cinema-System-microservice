CREATE TABLE IF NOT EXISTS tickets (
                                       id SERIAL PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       movie_name VARCHAR(255),
    seat_number VARCHAR(20),
    reservation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
