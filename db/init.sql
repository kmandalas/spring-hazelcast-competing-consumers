CREATE TABLE IF NOT EXISTS events (
    id BIGINT PRIMARY KEY,
    request_id VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    date_sent TIMESTAMP NOT NULL,
    date_stored TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS events_processed (
    request_id VARCHAR(255) PRIMARY KEY,
    status VARCHAR(255) NOT NULL,
    date_sent TIMESTAMP NOT NULL,
    date_processed TIMESTAMP NOT NULL,
    processed_by VARCHAR(255) NOT NULL
);
