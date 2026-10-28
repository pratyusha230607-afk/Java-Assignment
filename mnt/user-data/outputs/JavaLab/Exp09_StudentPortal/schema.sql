-- Run once in MySQL:  mysql -u root -p < schema.sql
CREATE DATABASE IF NOT EXISTS labdb;
USE labdb;

CREATE TABLE IF NOT EXISTS students (
    roll_no  VARCHAR(20)  PRIMARY KEY,
    name     VARCHAR(80)  NOT NULL,
    email    VARCHAR(100) NOT NULL,
    phone    VARCHAR(15)  NOT NULL,
    course   VARCHAR(50)  NOT NULL,
    gender   VARCHAR(10)  NOT NULL,
    address  VARCHAR(200)
);
