# OAuth 로그인 설정 가이드

## 1. Google OAuth 설정

### Google Cloud Console에서 앱 등록
1. [Google Cloud Console](https://console.cloud.google.com/) 접속
2. 새 프로젝트 생성 또는 기존 프로젝트 선택
3. 좌측 메뉴에서 "API 및 서비스" → "사용자 인증 정보" 클릭
4. "사용자 인증 정보 만들기" → "OAuth 클라이언트 ID" 선택
5. 애플리케이션 유형: "웹 애플리케이션" 선택
6. 설정 입력:
   - 이름: "Financial Blog" (원하는 이름)
   - 승인된 JavaScript 원본: 
     - `http://localhost:8080` (로컬 개발용)
     - `https://yourdomain.com` (프로덕션용)
   - 승인된 리디렉션 URI: 
     - `http://localhost:8080/login/oauth2/code/google` (로컬)
     - `https://yourdomain.com/login/oauth2/code/google` (프로덕션)
7. 생성 후 클라이언트 ID와 클라이언트 보안 비밀번호 복사

## 2. Kakao OAuth 설정

### Kakao Developers에서 앱 등록
1. [Kakao Developers](https://developers.kakao.com/) 접속
2. "내 애플리케이션" → "애플리케이션 추가하기"
3. 앱 이름, 회사명 입력 후 생성
4. 생성된 앱 선택 → "앱 설정" → "앱 키"에서 REST API 키 복사
5. "제품 설정" → "카카오 로그인" 활성화
6. "카카오 로그인" → "Redirect URI" 등록:
   - `http://localhost:8080/login/oauth2/code/kakao` (로컬)
   - `https://yourdomain.com/login/oauth2/code/kakao` (프로덕션)
7. "동의항목"에서 필요한 정보 설정:
   - 닉네임: 필수 동의
   - 이메일: 필수 동의

### Kakao 클라이언트 시크릿 생성
1. "앱 설정" → "보안"
2. "Client Secret" 생성 및 활성화
3. 생성된 시크릿 키 복사

## 3. Naver OAuth 설정

### Naver Developers에서 앱 등록
1. [Naver Developers](https://developers.naver.com/apps/#/register) 접속
2. "애플리케이션 등록" 클릭
3. 애플리케이션 정보 입력:
   - 애플리케이션 이름: "Financial Blog"
   - 사용 API: "네이버 로그인" 선택
   - 제공 정보 선택: 이메일, 이름 (필수)
4. 환경 설정:
   - PC 웹 선택
   - 서비스 URL: `http://localhost:8080`
   - 네이버 로그인 Callback URL 등록:
     - `http://localhost:8080/login/oauth2/code/naver` (로컬)
     - `https://yourdomain.com/login/oauth2/code/naver` (프로덕션)
5. 등록 완료 후 Client ID와 Client Secret 복사

## 4. GitHub OAuth 설정

### GitHub에서 OAuth App 등록
1. GitHub 로그인 → Settings → Developer settings
2. "OAuth Apps" → "New OAuth App" 클릭
3. 애플리케이션 정보 입력:
   - Application name: "Financial Blog"
   - Homepage URL: `http://localhost:8080` (나중에 프로덕션 URL로 변경 가능)
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. 등록 후 Client ID 복사
5. "Generate a new client secret" 클릭하여 Client Secret 생성 및 복사
6. **추가 Callback URL 등록** (GitHub는 하나의 앱에서 여러 URL 지원하지 않으므로):
   - 프로덕션용으로 별도 OAuth App 생성하거나
   - "Authorization callback URL"을 프로덕션 URL로 업데이트: `https://yourdomain.com/login/oauth2/code/github`

## 5. 환경 변수 설정

### 로컬 개발 환경

#### Windows (명령 프롬프트)
```cmd
set GOOGLE_CLIENT_ID=your-google-client-id
set GOOGLE_CLIENT_SECRET=your-google-client-secret
set KAKAO_CLIENT_ID=your-kakao-rest-api-key
set KAKAO_CLIENT_SECRET=your-kakao-client-secret
set NAVER_CLIENT_ID=your-naver-client-id
set NAVER_CLIENT_SECRET=your-naver-client-secret
set GITHUB_CLIENT_ID=your-github-client-id
set GITHUB_CLIENT_SECRET=your-github-client-secret
set APP_BASE_URL=http://localhost:8080
```

#### Windows (PowerShell)
```powershell
$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"
$env:KAKAO_CLIENT_ID="your-kakao-rest-api-key"
$env:KAKAO_CLIENT_SECRET="your-kakao-client-secret"
$env:NAVER_CLIENT_ID="your-naver-client-id"
$env:NAVER_CLIENT_SECRET="your-naver-client-secret"
$env:GITHUB_CLIENT_ID="your-github-client-id"
$env:GITHUB_CLIENT_SECRET="your-github-client-secret"
$env:APP_BASE_URL="http://localhost:8080"
```

#### macOS/Linux
```bash
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret
export KAKAO_CLIENT_ID=your-kakao-rest-api-key
export KAKAO_CLIENT_SECRET=your-kakao-client-secret
export NAVER_CLIENT_ID=your-naver-client-id
export NAVER_CLIENT_SECRET=your-naver-client-secret
export GITHUB_CLIENT_ID=your-github-client-id
export GITHUB_CLIENT_SECRET=your-github-client-secret
export APP_BASE_URL=http://localhost:8080
```

### IntelliJ IDEA 설정
1. Run → Edit Configurations
2. Environment variables 클릭
3. 위의 환경 변수들을 추가

## 6. 애플리케이션 실행

환경 변수 설정 후:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## 7. 프로덕션 배포 시 주의사항

### Redirect URI 변경
각 OAuth 제공자의 설정에서 Redirect URI를 프로덕션 URL로 변경:
- Google: `https://yourdomain.com/login/oauth2/code/google`
- Kakao: `https://yourdomain.com/login/oauth2/code/kakao`
- Naver: `https://yourdomain.com/login/oauth2/code/naver`
- GitHub: `https://yourdomain.com/login/oauth2/code/github`

### 프로덕션 환경 (Railway 등)

#### Railway 배포 시
Railway 대시보드에서 환경 변수 설정:
1. Railway 대시보드 → Variables 탭
2. 다음 환경 변수들 추가:
   - `GOOGLE_CLIENT_ID`: Google OAuth 클라이언트 ID
   - `GOOGLE_CLIENT_SECRET`: Google OAuth 클라이언트 시크릿
   - `KAKAO_CLIENT_ID`: Kakao REST API 키
   - `KAKAO_CLIENT_SECRET`: Kakao 클라이언트 시크릿
   - `NAVER_CLIENT_ID`: Naver 클라이언트 ID
   - `NAVER_CLIENT_SECRET`: Naver 클라이언트 시크릿
   - `GITHUB_CLIENT_ID`: GitHub 클라이언트 ID
   - `GITHUB_CLIENT_SECRET`: GitHub 클라이언트 시크릿
   - `APP_BASE_URL`: https://your-app-name.up.railway.app
   - `SPRING_PROFILES_ACTIVE`: production

#### 기타 클라우드 플랫폼
비슷하게 환경 변수를 설정하되, `APP_BASE_URL`을 해당 플랫폼의 도메인으로 설정

## 사용법

### 로컬 개발
1. 환경 변수 설정
2. 애플리케이션 실행: `mvn spring-boot:run -Dspring-boot.run.profiles=local`
3. `http://localhost:8080/login` 접속하여 OAuth 버튼들 확인

### 프로덕션 배포
1. 각 OAuth 제공자에서 프로덕션 Redirect URI 등록
2. 프로덕션 환경 변수 설정 (특히 `APP_BASE_URL`)
3. `SPRING_PROFILES_ACTIVE=production`으로 배포

## 동적 환경 설정의 장점

이제 **하나의 OAuth 앱 설정**으로 **로컬과 프로덕션 모두**에서 사용할 수 있습니다:

- **로컬**: `APP_BASE_URL=http://localhost:8080`
- **프로덕션**: `APP_BASE_URL=https://yourdomain.com`

각 OAuth 제공자에서 **두 개의 Redirect URI**를 등록하면 됩니다:
- `http://localhost:8080/login/oauth2/code/{provider}` (로컬용)  
- `https://yourdomain.com/login/oauth2/code/{provider}` (프로덕션용)

## 문제 해결

### 일반적인 오류
1. **redirect_uri_mismatch**: 
   - OAuth 제공자에 등록된 Redirect URI와 `APP_BASE_URL` 설정이 일치하는지 확인
   - 로컬은 `http://`, 프로덕션은 `https://` 프로토콜 확인
2. **invalid_client**: Client ID/Secret이 올바른지 확인
3. **access_denied**: 사용자가 권한 요청을 거부한 경우
4. **APP_BASE_URL 설정 오류**: 환경 변수가 올바르게 설정되었는지 확인

### 디버깅
`application.properties`에 다음 추가로 상세 로그 확인:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.oauth2=DEBUG
```

### 환경별 확인 방법
애플리케이션 실행 시 로그에서 다음 확인:
```
app.base-url=http://localhost:8080  # 로컬
app.base-url=https://yourdomain.com # 프로덕션
```