# 민달팽이 (Granite)

> 한국 부동산 법원경매 정보 플랫폼 Android 앱

법원경매 매물 검색부터 상세 분석, 관심 매물 관리까지 — 경매 투자에 필요한 정보를 한 곳에서 제공합니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Kotlin 2.3, Java 21 |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Hilt |
| **Network** | Retrofit + OkHttp (듀얼 클라이언트) |
| **Async** | Coroutines + Flow / StateFlow |
| **Local DB** | Room (검색 기록) + DataStore Preferences (설정) |
| **Map** | Kakao Maps SDK |
| **Image** | Landscapist (로딩, 플레이스홀더, 줌) |
| **Firebase** | Auth · FCM · Remote Config · Crashlytics · Analytics |
| **Social Login** | Kakao · Naver · Google · Apple |

## 주요 기능

**경매 매물 목록**
- 커서 기반 무한스크롤 페이지네이션
- Skeleton/Shimmer 로딩 UI

**바텀시트 필터 시스템**
- 지역, 건물유형, 유찰횟수, 감정가 범위, 정렬 조건 조합
- 필터 변경 시 실시간 매물 개수 프리뷰 (300ms 디바운스)

**매물 상세**
- 이미지 페이저, 경매 정보, 권리분석
- 경매장 카카오맵 위치 연동

**검색 & 관심 매물**
- 최근 검색어 Room DB 저장 (최대 100건 자동 관리)
- 관심 매물 등록/해제

**소셜 로그인 4종**
- Kakao, Naver, Google (Credential Manager), Apple (Firebase Auth)

**운영 기능**
- FCM 푸시 알림
- Remote Config 기반 강제 업데이트 (시맨틱 버전 비교)

## 아키텍처

```
┌─────────────────────────────────────────────┐
│                  UI Layer                   │
│         Compose Screen + ViewModel          │
│         (StateFlow, UiState)                │
├─────────────────────────────────────────────┤
│               Domain Layer                  │
│            UseCase + Model                  │
├─────────────────────────────────────────────┤
│                Data Layer                   │
│   Repository + Retrofit API + Room + DS     │
└─────────────────────────────────────────────┘
```

### 프로젝트 구조

```
app/src/main/java/com/estateslug/slug/
├── data/                # 데이터 레이어
│   ├── local/           #   Room DB, DataStore
│   └── network/         #   Retrofit API, Repository, RemoteConfig
├── domain/              # 도메인 레이어
│   ├── court/           #   법원 경매
│   ├── sales/           #   매물 상세
│   ├── search/          #   검색
│   └── user/            #   사용자, 관심 매물
├── ui/                  # 공통 UI 컴포넌트
│   ├── component/       #   재사용 컴포넌트, Skeleton/Shimmer
│   └── theme/           #   Material 3 테마
├── home/                # 홈 (매물 목록 + 필터)
├── detail/              # 매물 상세
├── search/              # 검색
├── favorite/            # 관심 매물
├── login/               # 소셜 로그인 (Apple, Google, Kakao, Naver)
├── mypage/              # 마이페이지
├── setting/             # 설정
├── main/                # MainActivity, Navigation
├── firebase/            # FCM
├── permission/          # 권한 처리
└── util/                # 유틸리티 (CursorPaginator 등)
```

## 기술적 특징

### Custom CursorPaginator\<T>

오프셋 대신 커서 기반 페이지네이션을 제네릭하게 처리하는 유틸리티.

- **스마트 리프레시** — 데이터가 동일하면 UI 상태를 교체하지 않아 불필요한 리컴포지션 방지
- **3단계 로딩 상태** — `isInitialLoading`, `isLoadingMore`, `isRefreshing` 분리
- **아이템 제거** — `removeItem(predicate)`로 낙관적 UI 업데이트

### 듀얼 OkHttp 클라이언트

Hilt Qualifier(`@WithAccessToken` / `@WithOutAccessToken`)로 인증·비인증 API 경로를 분리.

- 인증 클라이언트: Bearer 토큰 자동 주입 + 401/403 핸들링
- 커스텀 User-Agent 헤더 (앱 버전, OS, 디바이스 모델 포함)

### 바텀시트 필터 실시간 카운트

필터 옵션 변경 시 300ms 디바운스 후 서버에 매물 개수를 조회하여 "N개 매물 보기" 버튼에 실시간 반영. Job 기반 취소로 이전 요청을 자동 폐기.

### Shimmer 로딩 UI

`Modifier.shimmerEffect()` 확장으로 Skeleton 로딩 구현. InfiniteTransition 기반 그라디언트 애니메이션 (1000ms 주기).

## 빌드 방법

### 사전 준비

`local.properties`에 아래 키를 설정합니다:

```properties
KAKAO_APP_KEY="..."
KAKAO_APP_KEY_MANIFEST=...
APPLE_CLIENT_ID="..."
GOOGLE_APP_KEY="..."
NAVER_CLIENT_ID="..."
NAVER_CLIENT_SECRET="..."

BASE_URL = "..."
```

### 빌드 & 실행

```bash
# Debug 빌드
./gradlew assembleDebug

# 디바이스에 설치
./gradlew installDebug
```

| SDK | Version |
|-----|---------|
| Compile SDK | 36 (Android 15) |
| Target SDK | 36 |
| Min SDK | 28 (Android 9) |
