 CREATE TABLE IF NOT EXISTS urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS url_checks (
    id SERIAL PRIMARY KEY,
    url_id BIGINT REFERENCES urls(id) NOT NULL,
    status_code INT,
    title VARCHAR(1024),
    h1 VARCHAR(1024),
    description TEXT,
    created_at TIMESTAMP
);
