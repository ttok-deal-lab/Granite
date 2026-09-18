package com.estateslug.slug.ui.theme

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.data.local.device.LocalDeviceSettingDataRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 설정의 화면 테마([ThemeMode])를 저장소에서 읽어 앱에 적용한다.
 *
 * 적용은 두 겹이다.
 * 1. Compose: [SlugTheme]가 [mode]와 기기 다크 모드로 `darkTheme`를 정한다. 값이 바뀌면 화면이 바로 따라온다(API 전 구간).
 * 2. 시스템(API 31+): [UiModeManager.setApplicationNightMode]로 앱 단위 야간 모드를 기기에 기록한다.
 *    그러면 리소스(values-night 창 배경 = 시작 화면), `isSystemInDarkTheme()`, WebView까지 같은 값을 보고
 *    다음 실행의 첫 프레임부터 맞는다. 시스템은 이 값을 앱 데이터 삭제·제거 전까지 기억한다.
 *    라이트/다크는 MODE_NIGHT_NO/YES, 시스템 따름은 MODE_NIGHT_AUTO. 공개 문서는 AUTO를 위치·시간 기준으로 설명하지만
 *    앱 단위 설정에서는 기기의 다크 모드 설정을 그대로 따르는 것을 확인했다(API 36 에뮬레이터, 기기 위치 기준 밤 시간에도 라이트 유지).
 *    API 28~30은 1번만 적용되어 시작 화면 배경만 기기 설정을 따른다.
 */
@Singleton
class ThemeModeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val deviceSettings: LocalDeviceSettingDataRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    /**
     * 현재 테마. 저장값이 오기 전에는 [ThemeMode.DEFAULT](시스템 따름).
     * API 31+는 시스템이 앱 단위 야간 모드를 기억해 그 사이에도 첫 프레임이 맞고, 그 아래는 저장값이 기기 값과 다를 때
     * 첫 프레임 뒤 한 번 바뀔 수 있다. 시작 시 저장소를 동기로 읽어 막지는 않는다(느린 저장소에서 시작 지연·ANR 위험).
     * 저장소 오류로 흐름이 끊기면 앱 전체 테마가 멈추므로 기본값으로 대신한다.
     */
    val mode: StateFlow<ThemeMode> = deviceSettings.themeMode
        .catch { emit(ThemeMode.DEFAULT) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = ThemeMode.DEFAULT)

    /** 저장하고 시스템에 적용한다. 화면은 [mode]를 통해 따라온다. 같은 값이면 아무것도 하지 않는다(불필요한 재생성 방지). */
    suspend fun setMode(newMode: ThemeMode) {
        if (newMode == mode.value) return
        deviceSettings.setThemeMode(newMode).getOrThrow()
        applyToSystem(newMode)
    }

    /**
     * 앱 시작 시 저장값과 시스템의 앱 단위 야간 모드가 어긋나 있으면 맞춘다.
     * 보통은 시스템이 값을 기억하므로 아무 일도 하지 않는다(같은 값 재적용으로 화면이 다시 만들어지는 것을 피한다).
     */
    fun syncSystemOnStart() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        scope.launch {
            val stored = deviceSettings.themeMode.first()
            if (stored == ThemeMode.SYSTEM) return@launch
            val appIsNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
            if (appIsNight != (stored == ThemeMode.DARK)) applyToSystem(stored)
        }
    }

    private fun applyToSystem(newMode: ThemeMode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val uiModeManager = context.getSystemService(UiModeManager::class.java) ?: return
        uiModeManager.setApplicationNightMode(
            when (newMode) {
                ThemeMode.LIGHT -> UiModeManager.MODE_NIGHT_NO
                ThemeMode.DARK -> UiModeManager.MODE_NIGHT_YES
                ThemeMode.SYSTEM -> UiModeManager.MODE_NIGHT_AUTO
            }
        )
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ThemeModeEntryPoint {
        fun themeModeManager(): ThemeModeManager
    }
}

/**
 * 설정값과 기기 다크 모드로 앱이 다크로 그려야 하는지 정한다. [SlugTheme]의 기본값.
 * Preview(inspection)에서는 Hilt가 없으므로 기기 값만 쓴다.
 */
@Composable
fun rememberAppDarkTheme(): Boolean {
    val systemDark = isSystemInDarkTheme()
    if (LocalInspectionMode.current) return systemDark
    val appContext = LocalContext.current.applicationContext
    // Hilt 앱이 아닌 호스트(테스트 등)에서는 기기 값만 쓴다
    val controller = remember(appContext) {
        runCatching {
            EntryPointAccessors.fromApplication(appContext, ThemeModeManager.ThemeModeEntryPoint::class.java)
                .themeModeManager()
        }.getOrNull()
    } ?: return systemDark
    val mode by controller.mode.collectAsStateWithLifecycle()
    return when (mode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDark
    }
}

/**
 * 시스템 바 아이콘 색을 실제로 그리는 테마에 맞춘다.
 * 각 Activity의 `enableEdgeToEdge()` 기본값은 기기(또는 API 31+의 앱 단위) 야간 모드만 보므로,
 * 설정으로 테마를 바꾼 경우(특히 API 28~30)에는 여기서 다시 맞춘다. 같은 값이면 다시 적용해도 변화가 없다.
 */
@Composable
internal fun SyncSystemBarsWithTheme(darkTheme: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(darkTheme) {
        val activity = view.context.findActivity() as? ComponentActivity
        activity?.enableEdgeToEdge(
            statusBarStyle = if (darkTheme) SystemBarStyle.dark(TRANSPARENT) else SystemBarStyle.light(TRANSPARENT, TRANSPARENT),
            navigationBarStyle = if (darkTheme) SystemBarStyle.dark(DEFAULT_DARK_SCRIM) else SystemBarStyle.light(DEFAULT_LIGHT_SCRIM, DEFAULT_DARK_SCRIM),
        )
        onDispose { }
    }
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

private const val TRANSPARENT = android.graphics.Color.TRANSPARENT

// androidx.activity.EdgeToEdge의 기본 scrim과 같은 값 — 3버튼 내비 바(API 28) 대비용
private val DEFAULT_LIGHT_SCRIM = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DEFAULT_DARK_SCRIM = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
