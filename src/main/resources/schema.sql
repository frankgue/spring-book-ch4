CREATE TABLE account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         owner_name VARCHAR(100),
                         balance DOUBLE,
                         access_time TIMESTAMP,
                         locked BOOLEAN
);
