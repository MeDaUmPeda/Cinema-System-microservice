CREATE TABLE IF NOT EXISTS movies (
                                      id SERIAL PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    duration_minutes INT,
    release_date DATE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
