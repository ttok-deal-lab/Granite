package com.warehouseinhand.slug.setting.terms

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.warehouseinhand.slug.setting.ArrowSettingButton
import com.warehouseinhand.slug.setting.SettingTopBar

@Composable
fun TermsPage(onBackClick: () -> Unit) {//TODO : i18n
    val termsTitle = "서비스 약관"
    val userTermsTitle = "회원 이용약관"
    val userInfoTitle = "개인정보처리방침"
    val onUserTermClick = {}
    val onUserInfoTermClick = {}
    Surface {
        Column {
            SettingTopBar(text = termsTitle, onBackClick = onBackClick)
            ArrowSettingButton(buttonText = userTermsTitle, onClick = onUserTermClick)
            ArrowSettingButton(buttonText = userInfoTitle, onClick = onUserInfoTermClick)
        }
    }
}


@Composable
@Preview
private fun PreviewTermsPage() {
    val onBackClick: () -> Unit = {}
    TermsPage(onBackClick)
}