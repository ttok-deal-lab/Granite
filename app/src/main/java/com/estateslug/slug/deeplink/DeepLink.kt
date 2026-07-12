package com.estateslug.slug.deeplink

import android.net.Uri

/**
 * 딥링크 목적지. 서버 App Links(https://link.estateslug.com/...)와 FCM payload("link")를
 * 하나의 [DeepLinkResolver]로 해석한 결과.
 *
 * 지금은 중앙 라우터([DeepLinkRouterActivity])가 소비하지만, 추후 NavHost 통합 시에도
 * 이 resolver/destination은 그대로 재사용한다. (deeplink-architecture 결정)
 */
sealed interface DeepLinkDestination {
    data class Detail(val id: String) : DeepLinkDestination
    data class Search(val keyword: String) : DeepLinkDestination
    data class Tab(val tab: DeepLinkTab) : DeepLinkDestination
    data object Home : DeepLinkDestination
}

enum class DeepLinkTab { HOME, FAVORITE, MYPAGE }

object DeepLinkResolver {

    /** App Links 검증 대상 호스트. assetlinks.json은 https://link.estateslug.com/.well-known/ */
    const val HOST = "link.estateslug.com"

    /** 커스텀 스킴 (estateSlug://home/ 형태). 스킴 비교는 대소문자 무시. */
    const val SCHEME = "estateslug"

    /** KakaoLink 공유: kakao{APP_KEY}://kakaolink?route=sales/{id} (ShareKaKao의 executionParams와 계약) */
    const val KAKAO_SCHEME_PREFIX = "kakao"
    const val KAKAO_LINK_HOST = "kakaolink"
    const val KAKAO_ROUTE_PARAM = "route"

    /**
     * URI를 목적지로 해석한다.
     * - https://link.estateslug.com/sales/{id}  |  estateSlug://sales/{id}   → Detail(id)
     * - https://.../search?keyword={kw}         |  estateSlug://search?keyword={kw} → Search(kw)
     * - https://.../home|favorite|mypage        |  estateSlug://home/ 등     → Tab
     * - 그 외 / 파싱 실패 → Home (안전 폴백)
     *
     * https는 path가 라우트지만, 커스텀 스킴은 host 자리가 첫 라우트 토큰이다
     * (estateSlug://home/ 의 "home"은 Uri.host). 둘을 하나의 토큰 리스트로 정규화한다.
     */
    fun resolve(uri: Uri?): DeepLinkDestination {
        if (uri == null) return DeepLinkDestination.Home
        val scheme = uri.scheme?.lowercase()

        val segments: List<String> = when {
            scheme == "https" -> {
                if (uri.host != null && !uri.host.equals(HOST, ignoreCase = true)) {
                    return DeepLinkDestination.Home
                }
                uri.pathSegments
            }

            scheme == SCHEME -> listOfNotNull(uri.host) + uri.pathSegments

            // KakaoLink: route 파라미터가 라우트 토큰을 통째로 담는다 (예: route=sales/16400)
            scheme?.startsWith(KAKAO_SCHEME_PREFIX) == true &&
                uri.host.equals(KAKAO_LINK_HOST, ignoreCase = true) ->
                uri.getQueryParameter(KAKAO_ROUTE_PARAM)?.split('/').orEmpty()

            else -> uri.pathSegments
        }.filter { it.isNotBlank() }

        if (segments.isEmpty()) return DeepLinkDestination.Home

        return when (segments[0].lowercase()) {
            "sales" -> segments.getOrNull(1)
                ?.takeIf { it.isNotBlank() }
                ?.let { DeepLinkDestination.Detail(it) }
                ?: DeepLinkDestination.Home

            "search" -> uri.getQueryParameter("keyword")
                ?.takeIf { it.isNotBlank() }
                ?.let { DeepLinkDestination.Search(it) }
                ?: DeepLinkDestination.Home

            "home" -> DeepLinkDestination.Tab(DeepLinkTab.HOME)
            "favorite", "favorites" -> DeepLinkDestination.Tab(DeepLinkTab.FAVORITE)
            "mypage" -> DeepLinkDestination.Tab(DeepLinkTab.MYPAGE)
            else -> DeepLinkDestination.Home
        }
    }
}

/** 딥링크 관련 Intent extra / FCM payload 키. */
object DeepLinkKeys {
    /** LogInActivity가 인증 후 이어보낼 원본 딥링크 URI(String). */
    const val PENDING_DEEPLINK = "PENDING_DEEPLINK"

    /** MainActivity 초기 탭 (DeepLinkTab.name). */
    const val START_TAB = "START_TAB"

    /** SearchActivity 초기 검색어. */
    const val SEARCH_KEYWORD = "SEARCH_KEYWORD"

    /** FCM data payload에서 딥링크 URL을 담는 키. */
    const val FCM_LINK = "link"
}
