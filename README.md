# 금융 블로그 (Financial Blog)

Spring Boot 기반 금융 정보 공유 블로그

## 기술 스택
- Spring Boot 3.x
- Thymeleaf
- PostgreSQL
- Spring Security
- Tailwind CSS (CDN)

## Railway 배포 방법

1. GitHub에 코드 푸시
2. Railway.app에서 새 프로젝트 생성
3. GitHub 저장소 연결
4. PostgreSQL 데이터베이스 추가
5. 환경변수 설정:
   - `DATABASE_URL` (자동 설정됨)
   - `RAILWAY_ENVIRONMENT=production`

## 로컬 실행
```bash
mvn spring-boot:run
```

## 기본 관리자 계정
- Username: admin
- Password: admin123