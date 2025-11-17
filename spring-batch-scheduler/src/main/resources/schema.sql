-- Database Schema for Users Table
-- Interview Point: This creates the table where we'll store processed data

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT,
    department VARCHAR(50)
);

-- Interview Point: This SQL file runs automatically on startup
-- Spring Boot executes schema.sql if it exists in resources folder

