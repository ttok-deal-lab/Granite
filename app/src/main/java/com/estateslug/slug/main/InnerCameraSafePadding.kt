package com.estateslug.slug.main

import android.os.Build
import android.view.Surface
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

/**
 * 펼친 폴더블 내부 화면의 카메라 구멍을 피하는 좌우 여백.
 *
 * 삼성은 내부 화면 카메라 영역을 앱에 displayCutout 인셋으로 알려주지 않는다 — WM 내부의 udcCutout만
 * 있고 앱 창 InsetsState에는 어느 회전에서도 displayCutout 소스가 없다 (2026-09-16 SM-F976N 실측).
 * 세로(힌지 세로)에선 상태바가 카메라를 덮지만, 가로에선 카메라가 자연 방향의 위쪽이 놓인 측면
 * 가장자리(ROTATION_90=왼쪽, ROTATION_270=오른쪽)로 와 레일·상세 페인 위에 걸린다.
 * 시스템이 컷아웃을 선언했을 때 주는 인셋과 같은 모양으로 그 쪽에만 여백을 준다.
 *
 * UDC(언더디스플레이 카메라) 모델은 그 영역에도 픽셀이 있어 그려도 되므로 제외하고, 실제 구멍인
 * 모델만 처리한다. 접힘(커버 화면)·세로·타사 기기는 0 — 커버 화면은 시스템이 컷아웃을 정상 선언해
 * MainScreen의 displayCutout 인셋 패딩이 담당한다.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun rememberInnerCameraSafePadding(): PaddingValues {
    val unfolded = currentWindowAdaptiveInfoV2().windowPosture.hingeList.isNotEmpty()
    val configuration = LocalConfiguration.current
    val view = LocalView.current
    // 회전은 구성 변경으로 액티비티가 재생성되지만, configChanges를 붙이는 경우에도 갱신되도록 구성에 키를 건다
    val rotation = remember(configuration) { view.display?.rotation ?: Surface.ROTATION_0 }
    return remember(unfolded, rotation) {
        when {
            !unfolded || !hasInnerPunchHoleCamera() -> PaddingValues(0.dp)
            rotation == Surface.ROTATION_90 -> PaddingValues.Absolute(left = INNER_CAMERA_SAFE_MARGIN)
            rotation == Surface.ROTATION_270 -> PaddingValues.Absolute(right = INNER_CAMERA_SAFE_MARGIN)
            else -> PaddingValues(0.dp)
        }
    }
}

/** 자연 방향에서 카메라 구멍이 차지하는 상단 띠(실측 104px@480dpi ≈ 35dp)를 덮는 값 */
private val INNER_CAMERA_SAFE_MARGIN = 36.dp

/**
 * 내부 카메라가 실제 구멍인 삼성 폴더블 모델 접두. UDC 모델(Fold3 SM-F926 · Fold4 SM-F936 ·
 * Fold5 SM-F946 · Fold6 SM-F956)은 의도적으로 제외. 새 모델이 나오면 여기에 추가한다.
 */
private val INNER_PUNCH_HOLE_MODEL_PREFIXES = listOf(
    "SM-F966", // Galaxy Z Fold7 — UDC 대신 펀치홀로 전환
    "SM-F976", // Galaxy Z Fold8 (실측 기기)
)

private fun hasInnerPunchHoleCamera(): Boolean =
    Build.MANUFACTURER.equals("samsung", ignoreCase = true) &&
        INNER_PUNCH_HOLE_MODEL_PREFIXES.any { Build.MODEL.startsWith(it, ignoreCase = true) }
