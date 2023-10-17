-- hittracker 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS hittracker;
USE hittracker;

-- Url 테이블 생성
CREATE TABLE IF NOT EXISTS Url (
    url_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL UNIQUE
);

-- DailyHitLog 테이블 생성
CREATE TABLE IF NOT EXISTS DailyHitLog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    dailyHit INT NOT NULL,
    url_id BIGINT NOT NULL,
    FOREIGN KEY (url_id) REFERENCES Url(url_id) ON DELETE CASCADE
);
