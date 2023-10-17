# 넘블 DeepDive - 방문자 수 트래킹 서비스 구축하기

## 프로젝트 실행하는 방법

```bash
docker-compose up -d
```

위 명령어를 실행하여 mysql, redis 환경을 구성한 뒤 실행하시면 바로 프로젝트를 실행 및 테스트 하실 수 있습니다.

## 구조도

<img width="557" alt="기술스택" src="https://github.com/deepredk/hit-tracker/assets/33937365/d465b46d-1e36-44c0-82f1-ef61f5736041">

## 테이블 DDL

```sql
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

```

## 코드 구현 설명

### POST /count?url={url}

- 원자성이 보장되는 Redis의 incr 명령어를 이용하여 총 조회수와 일간 조회수를 보관합니다.
  - 일간 조회수, 총 조회수는 오직 Redis에서만 보관합니다.
- Redis쪽에 요청보내는 것은 동기적으로 실행하지 않고 비동기적으로 실행합니다.
- 처음 요청된 url이라면 MySQL의 Url 테이블에 행을 추가합니다.
  - Redis의 sadd 명령어를 이용하여 처음 요청됐는지 확인합니다

### GET /count?url={url}

- Redis에서 일간 조회수와 총 조회수를 읽어옵니다.
- 처음 요청된 url이라면 MySQL의 Url 테이블에 행을 추가합니다.

### GET /statistics?url={url}

- MySQL에서 최근 7일간의 일간 조회수를 읽어옵니다.

### 매일 자정마다 일간 조회수 초기화 및 저장

매일 자정마다 `@Scheduled`를 이용해 아래를 수행합니다.
1. MySQL Url 테이블의 모든 행을 읽는다
2. 읽은 행들의 url 컬럼의 값을 이용하여 일간조회수를 불러온 뒤 DailyHitLog에 행을 추가한다
3. Redis의 일간조회수 값을 0으로 설정한다
4. 7일이 지난 DailyHitLog는 삭제한다

### Redis를 사용한 이유

- 원자성이 보장되는 incr 오퍼레이션으로 동시성 이슈를 해결하면서 Redis의 좋은 성능까지도 활용하기 위해 채택했습니다.
