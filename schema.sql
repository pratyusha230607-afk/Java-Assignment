-- Run once in MySQL:  mysql -u root -p < schema.sql
CREATE DATABASE IF NOT EXISTS labdb;
USE labdb;

CREATE TABLE IF NOT EXISTS users (
    name     VARCHAR(50)  PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);
