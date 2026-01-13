package com.warehouseinhand.slug.setting.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.warehouseinhand.slug.setting.ArrowSettingButton
import com.warehouseinhand.slug.setting.SettingTopBar
import com.warehouseinhand.slug.ui.component.WebView
import com.warehouseinhand.slug.ui.theme.NeutralInverted

@Composable
fun TermsPage(onBackClick: () -> Unit) {//TODO : i18n
    val termsTitle = "서비스 약관"
    val serviceTermsTitle = "회원 이용약관"
    val userInfoTitle = "개인정보처리방침"
    val navController = rememberNavController()

    val onServiceTermClick = {
        navController.navigate(TERMS_SERVICE)
    }
    val onUserInfoTermClick = {
        navController.navigate(TERMS_USER_INFO)
    }

    Surface {
        NavHost(navController = navController, startDestination = TERMS_LIST) {
            composable(TERMS_LIST) {
                Column(
                    modifier = Modifier
                        .background(NeutralInverted)
                        .fillMaxSize()
                ) {
                    SettingTopBar(text = termsTitle, onBackClick = onBackClick)
                    ArrowSettingButton(buttonText = serviceTermsTitle, onClick = onServiceTermClick)
                    ArrowSettingButton(buttonText = userInfoTitle, onClick = onUserInfoTermClick)
                }
            }
            composable(TERMS_SERVICE) {
                Column(
                    modifier = Modifier
                        .background(NeutralInverted)
                        .fillMaxSize()
                ) {
                    SettingTopBar(text = "", onBackClick = { navController.navigateUp() })
                    ServiceTermsPage(url = SERVICE_URL)
                }

            }
            composable(TERMS_USER_INFO) {
                Column(
                    modifier = Modifier
                        .background(NeutralInverted)
                        .fillMaxSize()
                ) {
                    SettingTopBar(text = "", onBackClick = { navController.navigateUp() })
                    ServiceTermsPage(url = USER_URL)
                }
            }

        }
    }
}

@Composable
fun ServiceTermsPage(url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        WebView(
            modifier = Modifier.fillMaxSize(),
            url = url
        )
    }
}


private const val TERMS_LIST = "TERMS_LIST"
private const val TERMS_SERVICE = "TERMS_SERVICE"
private const val TERMS_USER_INFO = "TERMS_SERVICE"
private const val SERVICE_URL =
    "https://acoustic-bovid-71a.notion.site/v1-2ba5ff9f915c80f4b458ef4d48083820"
private const val USER_URL =
    "https://acoustic-bovid-71a.notion.site/AOS-ver-1-2ba5ff9f915c80f2965deb5e144b251b"

@Composable
@Preview
private fun PreviewTermsPage() {
    val onBackClick: () -> Unit = {}
    TermsPage(onBackClick)
}