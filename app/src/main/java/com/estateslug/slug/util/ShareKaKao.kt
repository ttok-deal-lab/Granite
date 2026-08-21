package com.estateslug.slug.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import com.estateslug.slug.R
import com.estateslug.slug.detail.ShareItem
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link


//TODO : 타 SNS 공유 지원은 차회에

fun shareKakao(item: ShareItem, context: Context) {
    val template = item.toKaKaoFeed()
// 카카오톡 설치여부 확인
    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
//         카카오톡 실행해 카카오톡 공유
        ShareClient.instance.shareDefault(
            context = context,
            defaultTemplate = item.toKaKaoFeed(),
        ) { sharingResult, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 공유 실패", error)
            } else if (sharingResult != null) {
                Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                context.startActivity(sharingResult.intent)
                // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
            }
        }
    } else {
        // 카카오톡 미설치: 웹 공유 사용 권장
        // 웹 공유 예시 코드
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(template = template)
        // CustomTabs으로 웹 브라우저 열기
        // 1. CustomTabsServiceConnection 지원 브라우저 열기
        // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
        } catch (e: UnsupportedOperationException) {
            // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
        }
        // 2. CustomTabsServiceConnection 미지원 브라우저 열기
        // ex) 다음, 네이버 등
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
        }
    }
}

/** 시스템 공유 시트로 링크 직접 공유 — 카카오 피드와 동일한 App Links URL 사용 */
fun shareLink(item: ShareItem, context: Context) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.detail_share_text,
                item.nameOfProduct,
                item.caseNumber,
                item.webDetailUrl(),
            )
        )
        // 공유 시트 상단 미리보기 제목 (지원 앱에서만 표시)
        putExtra(Intent.EXTRA_TITLE, item.nameOfProduct)
    }
    context.startActivity(Intent.createChooser(sendIntent, null))
}

private fun ShareItem.shareUrl(): String = "$shareLinkBase/sales/$id"

/** 링크 직접 공유용 웹 프론트 상세 페이지 URL (카카오 피드의 webUrl은 콘솔 등록 도메인이라 별도 유지) */
private fun ShareItem.webDetailUrl(): String = "$webDetailLinkBase/detail/$id"

private fun ShareItem.toKaKaoFeed(): FeedTemplate {
    val shareLink = shareUrl()
    // 앱 설치 시: 카카오톡이 kakao{APP_KEY}://kakaolink?route=sales/{id} 로 앱 실행
    //   → DeepLinkRouterActivity가 DeepLinkResolver.KAKAO_ROUTE_PARAM 계약으로 상세 진입
    // 미설치 시: webUrl(https://link.estateslug.com/...)로 폴백
    val executionParams = mapOf("route" to "sales/${this.id}")
    val link = Link(
        webUrl = shareLink,
        mobileWebUrl = shareLink,
        androidExecutionParams = executionParams,
        iosExecutionParams = executionParams,
    )
    return FeedTemplate(
        content = Content(
            title = this.nameOfProduct,
            description = "사건번호: $caseNumber",
            imageUrl = imageUrl,
            link = link
        ),
        buttons = listOf(
            Button(
                "자세히 보기",
                link
            )
        )
    )
}


private val TAG = "shareKakao"
const val shareLinkBase = "https://link.estateslug.com"
//TODO : 배포 환경 분리 시 BuildConfig로 이동
const val webDetailLinkBase = "https://ttok-front-dev.estateslug.com"
