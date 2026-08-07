package com.estateslug.slug.detail.subpage.auction

import androidx.annotation.StringRes
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.SlugText

/**
 * 서버 CourtName enum name("INCHEON_DISTRICT" 등) → 한글 문자열 리소스.
 * 매핑에 없는 값(신규 법원 등)은 서버 원문을 그대로 노출한다.
 */
fun courtDisplayName(serverName: String): SlugText =
    CourtNameRes.entries.find { it.name == serverName }
        ?.let { SlugText.Id(it.resId) }
        ?: SlugText.Text(serverName)

/** 서버 CourtName enum과 1:1 — 상수명이 서버 name과 정확히 일치해야 한다 */
private enum class CourtNameRes(@StringRes val resId: Int) {
    SEOUL_CENTRAL_DISTRICT(R.string.court_name_seoul_central_district),
    SEOUL_EAST_DISTRICT(R.string.court_name_seoul_east_district),
    SEOUL_WEST_DISTRICT(R.string.court_name_seoul_west_district),
    SEOUL_SOUTH_DISTRICT(R.string.court_name_seoul_south_district),
    SEOUL_NORTH_DISTRICT(R.string.court_name_seoul_north_district),
    UIJEONGBU_DISTRICT(R.string.court_name_uijeongbu_district),
    GOYANG_BRANCH(R.string.court_name_goyang_branch),
    NAMYANGJU_BRANCH(R.string.court_name_namyangju_branch),
    INCHEON_DISTRICT(R.string.court_name_incheon_district),
    BUCHEON_BRANCH(R.string.court_name_bucheon_branch),
    SUWON_DISTRICT(R.string.court_name_suwon_district),
    SEONGNAM_BRANCH(R.string.court_name_seongnam_branch),
    YEOJU_BRANCH(R.string.court_name_yeoju_branch),
    PYEONGTAEK_BRANCH(R.string.court_name_pyeongtaek_branch),
    ANSAN_BRANCH(R.string.court_name_ansan_branch),
    ANYANG_BRANCH(R.string.court_name_anyang_branch),
    CHUNCHEON_DISTRICT(R.string.court_name_chuncheon_district),
    GANGNEUNG_BRANCH(R.string.court_name_gangneung_branch),
    WONJU_BRANCH(R.string.court_name_wonju_branch),
    SOKCHO_BRANCH(R.string.court_name_sokcho_branch),
    YEONGWOL_BRANCH(R.string.court_name_yeongwol_branch),
    CHEONGJU_DISTRICT(R.string.court_name_cheongju_district),
    CHUNGJU_BRANCH(R.string.court_name_chungju_branch),
    JECHEON_BRANCH(R.string.court_name_jecheon_branch),
    YEONGDONG_BRANCH(R.string.court_name_yeongdong_branch),
    DAEJEON_DISTRICT(R.string.court_name_daejeon_district),
    HONGSEONG_BRANCH(R.string.court_name_hongseong_branch),
    NONSAN_BRANCH(R.string.court_name_nonsan_branch),
    CHEONAN_BRANCH(R.string.court_name_cheonan_branch),
    GONGJU_BRANCH(R.string.court_name_gongju_branch),
    SEOSAN_BRANCH(R.string.court_name_seosan_branch),
    DAEGU_DISTRICT(R.string.court_name_daegu_district),
    ANDONG_BRANCH(R.string.court_name_andong_branch),
    GYEONGJU_BRANCH(R.string.court_name_gyeongju_branch),
    GIMCHEON_BRANCH(R.string.court_name_gimcheon_branch),
    SANGJU_BRANCH(R.string.court_name_sangju_branch),
    UISEONG_BRANCH(R.string.court_name_uiseong_branch),
    YEONGDEOK_BRANCH(R.string.court_name_yeongdeok_branch),
    POHANG_BRANCH(R.string.court_name_pohang_branch),
    DAEGU_WEST_BRANCH(R.string.court_name_daegu_west_branch),
    BUSAN_DISTRICT(R.string.court_name_busan_district),
    BUSAN_EAST_BRANCH(R.string.court_name_busan_east_branch),
    BUSAN_WEST_BRANCH(R.string.court_name_busan_west_branch),
    ULSAN_DISTRICT(R.string.court_name_ulsan_district),
    CHANGWON_DISTRICT(R.string.court_name_changwon_district),
    MASAN_BRANCH(R.string.court_name_masan_branch),
    JINJU_BRANCH(R.string.court_name_jinju_branch),
    TONGYEONG_BRANCH(R.string.court_name_tongyeong_branch),
    MILYANG_BRANCH(R.string.court_name_milyang_branch),
    GEOCHANG_BRANCH(R.string.court_name_geochang_branch),
    GWANGJU_DISTRICT(R.string.court_name_gwangju_district),
    MOKPO_BRANCH(R.string.court_name_mokpo_branch),
    JANGHEUNG_BRANCH(R.string.court_name_jangheung_branch),
    SUNCHEON_BRANCH(R.string.court_name_suncheon_branch),
    HAENAM_BRANCH(R.string.court_name_haenam_branch),
    JEONJU_DISTRICT(R.string.court_name_jeonju_district),
    GUNSAN_BRANCH(R.string.court_name_gunsan_branch),
    JEONGEUP_BRANCH(R.string.court_name_jeongeup_branch),
    NAMWON_BRANCH(R.string.court_name_namwon_branch),
    JEJU_DISTRICT(R.string.court_name_jeju_district),
}
