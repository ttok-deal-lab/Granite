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

    /**
     * URI를 목적지로 해석한다.
     * - /sales/{id}            → Detail(id)
     * - /search?keyword={kw}   → Search(kw)
     * - /home | /favorite | /mypage → Tab
     * - 그 외 / 파싱 실패        → Home (안전 폴백)
     *
     * https 스킴인데 host가 우리 것이 아니면 Home으로 폴백한다.
     */
    fun resolve(uri: Uri?): DeepLinkDestination {
        if (uri == null) return DeepLinkDestination.Home
        if (uri.scheme.equals("https", ignoreCase = true) &&
            uri.host != null && !uri.host.equals(HOST, ignoreCase = true)
        ) {
            return DeepLinkDestination.Home
        }

        val segments = uri.pathSegments.filter { it.isNotBlank() }
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
