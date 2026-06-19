## 🐶 댕냥

<p align="center">
  <img src="./docs/image/daenynyang.png" alt="댕냥 서비스 로고" width="720">
</p>

댕냥은 반려동물을 함께 키우는 사람들이 그룹을 이루어, 반려동물의 일상과 건강 상태를 기록하고 공유하는 서비스입니다

## 주요 기능

🐶 **혼자 하는 반려동물 기록이 아니라, 함께 하는 서비스**

1️⃣ **그룹 기반 반려동물 공동 관리**

`OWNER/MEMBER` 역할과 그룹 가입 신청·승인 흐름을 통해 그룹 단위 케어 및 일상 기록 시나리오를 제공합니다

2️⃣ **날짜별 게시글 업로드**

반려동물별 사진 또는 동영상을 날짜 기준으로 기록합니다

3️⃣ **`DailyLog` 기반 반려동물 데이터 관리**  

체중, 식사량, 음수량, 산책 거리와 시간, 수면 시간, 배변·배뇨 횟수, 구토·설사 횟수, 투약 여부 등 일일 건강 지표를 기록합니다

4️⃣ **주간·월간 건강 리포트 자동 생성**

누적된 `DailyLog` 데이터를 기반으로 평균 체중, 평균 식사량, 총 산책 거리, 이상 증상 발생 일수 등을 집계하고, 스케줄러를 통해 주간·월간 리포트를 자동 생성합니다

5️⃣ **주간·월간 건강 리포트 자동 생성**  
누적된 DailyLog 데이터를 기반으로 평균 체중, 평균 식사량, 총 산책 거리, 이상 증상 발생 일수 등을 집계합니다. 스케줄러를 통해 주간·월간 리포트를 자동 생성하고, 통계 쿼리와 배치 insert로 대량 리포트 생성 성능을 개선했습니다.

## 기술 스택

기술 선택은 단순 도입이 아니라, 정합성, 조회 성능, 대량 집계 처리, 캐시 일관성 문제를 해결하기 위한 목적 중심으로 진행했습니다

| 분류 | 기술 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.5.7 |
| ORM | Spring Data JPA |
| Database | MySQL 8.0 |
| Security | Spring Security, JWT |
| Cache | Redis |
| File Storage | AWS S3 |
| Scheduler | Spring Scheduler |
| Async | Spring Async, TransactionalEventListener |

## ERD

<p align="center">
  <img src="./docs/image/daenynyang-erd.png" alt="댕냥 ERD" width="900">
</p>