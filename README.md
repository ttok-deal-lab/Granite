# 민달팽이 (Granite)

> 한국 부동산 법원경매 정보 플랫폼 Android 앱

법원경매 매물 검색부터 상세 정보 확인, 관심 매물 관리까지 — 경매 투자에 필요한 정보를 한 곳에서 제공합니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Kotlin 2.3, Java 21 |
| **UI** | Jetpack Compose + Material 3 (라이트 테마 고정 — 다크 디자인 부재) |
| **Navigation** | Navigation Compose type-safe route (kotlinx-serialization) |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Hilt |
| **Network** | Retrofit + OkHttp (듀얼 클라이언트) |
| **Async** | Coroutines + Flow / StateFlow |
| **Local DB** | Room (검색 기록) + DataStore Preferences (설정, 최근 본 매물) |
| **Map** | Kakao Maps SDK |
| **Image** | Landscapist (로딩, 플레이스홀더, 줌) |
| **Firebase** | Auth · FCM · Remote Config · Crashlytics · Analytics · Performance |
| **Social Login** | Kakao · Naver · Google · Apple |
| **기타** | Kakao Share (v2-all), play-services-oss-licenses (설정 › 오픈소스 라이선스) |

## 주요 기능

**경매 매물 목록**
- 커서 기반 무한스크롤 페이지네이션
- Skeleton/Shimmer 로딩 UI
- D-day 칩 — 매각기일 기준 D-/D+/매각 상태 표시 (`DDayState` sealed)
- 매물 수 상한 표기 — Elasticsearch `track_total_hits` 상한 초과 시 "9,999+"

**바텀시트 필터 시스템**
- 지역, 건물유형, 유찰횟수, 감정가 범위, 정렬 조건 조합
- 필터 변경 시 실시간 매물 개수 프리뷰 (300ms 디바운스)

**매물 상세**
- 이미지 페이저, 경매 정보, 최근 실거래 (없으면 "최근 실거래 없음")
- 경매장(관할법원) 카카오맵 위치 연동
- 권리분석 — MVP 미적용 (화면·데이터 코드는 유지, `DetailScreen.kt` 탭 목록에서 주석 처리)

**공유 & 딥링크**
- 카카오톡 공유 — 피드 템플릿, 매물 첫 이미지 포함
- 링크 공유 — 시스템 공유 시트로 웹 상세 URL 전달
- App Links `https://link.estateslug.com/sales/{id}` → `DeepLinkRouterActivity` 중앙 라우터 → 상세 진입

**검색 & 관심 매물**
- 최근 검색어 Room DB 저장 (최대 100건 자동 관리)
- 최근 본 매물 DataStore 저장 (최대 10건)
- 관심 매물 등록/해제

**소셜 로그인 4종**
- Kakao, Naver, Google (Credential Manager), Apple (Firebase Auth)

**운영 기능**
- FCM 푸시 알림 기반 구축 (서비스·권한·알림 설정 화면) — MVP에서는 발송 미제공
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
│   ├── favorite/        #   FavoriteStateStore (화면 간 관심 상태 공유)
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
│   └── recent/          #   최근 본 매물
├── setting/             # 설정
├── main/                # MainActivity, 탭 Navigation
├── deeplink/            # App Links 라우터 (DeepLinkRouterActivity)
├── firebase/            # FCM
├── permission/          # 권한 처리
└── util/                # 유틸리티 (CursorPaginator, ShareKaKao 등)
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

### 탭 네비게이션 — multiple back stacks

탭 전환은 `popUpTo(findStartDestination) { saveState = true }` + `restoreState` + `launchSingleTop` 표준 패턴을 따릅니다. 어느 탭에서든 back은 홈으로 수렴하고, 탭별 스크롤 위치·ViewModel은 보존됩니다.

### 딥링크 중앙 라우터

App Links는 단일 진입점 `DeepLinkRouterActivity`가 URI를 해석해 `MainActivity`로 위임합니다. 최근 앱 목록에서 재실행될 때 태스크의 base intent가 재생되어 상세로 다시 진입하는 문제는 `FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY` 가드로 차단합니다.

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

# server per buildType: debug -> BASE_URL, release -> BASE_URL_RELEASE (release build fails without it)
BASE_URL = "..."
BASE_URL_RELEASE = "..."

# release signing (optional): all 4 required to sign, otherwise warns and builds unsigned
KEYSTORE_FILE=...
KEYSTORE_PASSWORD=...
KEY_ALIAS=...
KEY_PASSWORD=...
```

### 빌드 & 실행

```bash
# Debug 빌드
./gradlew assembleDebug

# 디바이스에 설치
./gradlew installDebug

# 스토어 제출용 AAB → app/build/outputs/bundle/release/app-release.aab
./gradlew bundleRelease
```

- release 빌드에만 R8 minify·리소스 shrink가 적용됩니다 (`proguard-rules.pro`는 두 buildType이 공유).
- `abiFilters`는 ARM 2종(`armeabi-v7a`, `arm64-v8a`)만 포함합니다. Kakao Maps 네이티브 라이브러리가 ARM 전용이라 x86 기기 시작 크래시를 막기 위한 설정이니 제거하지 마세요.

| SDK | Version |
|-----|---------|
| Compile SDK | 36 (Android 16) |
| Target SDK | 36 |
| Min SDK | 28 (Android 9) |
