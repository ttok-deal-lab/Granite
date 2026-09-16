package com.estateslug.slug.ui.theme

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge

/**
 * 라이트 테마 고정 앱용 edge-to-edge. 모든 Activity는 기본 [enableEdgeToEdge] 대신 이것을 쓴다.
 *
 * 인자 없는 [enableEdgeToEdge]는 시스템 다크모드 여부로 시스템바 아이콘 색을 정한다(다크면 흰색).
 * [SlugTheme]는 다크 디자인 없이 항상 밝은 화면을 그리므로, 다크모드 폰에서는 흰 상태바 아이콘이
 * 흰 배경에 묻혀 보이지 않았다(2026-09-17 보고, 초기 커밋부터 있던 잠복 버그). 원칙은 하나다 —
 * 시스템바 아이콘 색은 앱이 실제로 그리는 배경과 맞아야 한다.
 *
 * ## 다크 테마 도입 시 롤백 절차
 * 1. 호출부 5곳(MainActivity, SearchActivity, RecentItemsActivity, SettingActivity, LogInActivity)의
 *    `enableLightEdgeToEdge()`를 `enableEdgeToEdge()`로 되돌리고 import를 `androidx.activity.enableEdgeToEdge`로 교체.
 * 2. 이 파일 삭제. 기본값은 시스템 다크모드 = 앱 다크 화면이면 올바르게 동작한다.
 * 3. 예외 — 앱 안에 테마 선택(라이트/다크/시스템 따름)을 두면 기본값이 다시 어긋난다(시스템은 라이트,
 *    앱만 다크 가능). 그때는 앱이 고른 테마로 `SystemBarStyle.dark(TRANSPARENT)`/`light(...)`를 직접 넘기거나,
 *    `UiModeManager.setApplicationNightMode`(API 31+)로 앱 단위 야간 모드를 설정해 구성 자체를 바꾼다.
 * 4. XML 테마 `Theme.Slug`(android:Theme.Material.Light.NoActionBar)도 DayNight 계열로 바꿔야
 *    첫 프레임 전 창 배경이 흰색으로 번쩍이지 않는다.
 * 5. 검증 — `adb shell cmd uimode night yes` → 앱 force-stop → 콜드 스타트 → 상태바 아이콘이 보이는지 확인.
 *    `dumpsys window windows`의 앱 창 `apr=`에 LIGHT_STATUS_BARS가 없어야 다크 화면에 맞는 상태(흰 아이콘)다.
 * 백로그: docs/release/deferred-features.md "다크모드" 항목.
 */
fun ComponentActivity.enableLightEdgeToEdge() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(scrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.light(scrim = NAV_BAR_LIGHT_SCRIM, darkScrim = NAV_BAR_DARK_SCRIM),
    )
}

// enableEdgeToEdge 기본 내비바 스크림과 같은 값 — activity 라이브러리의 DefaultLightScrim/DefaultDarkScrim이 private
private val NAV_BAR_LIGHT_SCRIM = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val NAV_BAR_DARK_SCRIM = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
