@echo off
echo OAuth 로그인 테스트를 위한 환경 설정

echo.
echo 1. 환경 변수 설정 (예시)
echo set GOOGLE_CLIENT_ID=your-google-client-id
echo set GOOGLE_CLIENT_SECRET=your-google-client-secret
echo set KAKAO_CLIENT_ID=your-kakao-rest-api-key
echo set KAKAO_CLIENT_SECRET=your-kakao-client-secret
echo set NAVER_CLIENT_ID=your-naver-client-id
echo set NAVER_CLIENT_SECRET=your-naver-client-secret
echo set GITHUB_CLIENT_ID=your-github-client-id
echo set GITHUB_CLIENT_SECRET=your-github-client-secret
echo set APP_BASE_URL=http://localhost:8080

echo.
echo 2. 위의 환경 변수들을 실제 값으로 설정한 후
echo    애플리케이션을 실행하세요:
echo.
echo mvn spring-boot:run -Dspring-boot.run.profiles=local

echo.
echo 3. 브라우저에서 다음 URL로 접속하여 테스트:
echo http://localhost:8080/login

echo.
echo 4. 각 OAuth 버튼을 클릭하여 로그인이 정상 작동하는지 확인

pause