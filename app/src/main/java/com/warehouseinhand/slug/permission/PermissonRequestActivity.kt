package com.warehouseinhand.slug.permission

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.main.MainActivity
import com.warehouseinhand.slug.permission.PermissionDataModel.Companion.REQUEST_MAX_LIMIT
import com.warehouseinhand.slug.ui.component.button.basic.BasicButton
import com.warehouseinhand.slug.ui.theme.Gray400
import com.warehouseinhand.slug.ui.theme.Gray400TextSubtext
import com.warehouseinhand.slug.ui.theme.Gray50
import com.warehouseinhand.slug.ui.theme.Gray700
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PermissionRequestActivity : ComponentActivity() {
    private var permissionAllowedToExit = MutableStateFlow(false)
    private val permissionChecker =
        PermissionChecker(activity = this) { essentialFailed, _ ->
            lifecycleScope.launch {
                permissionAllowedToExit.emit(essentialFailed.isEmpty())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SlugTheme {
                // A surface container using the 'background' color from the theme
                PermissionRequestPage(
                    essential = PermissionDataModel.essentialPermissions,
                    optional = PermissionDataModel.optionalPermissions
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            permissionAllowedToExit.emit(PermissionChecker.isAllOfEssentialAllowed(this@PermissionRequestActivity))
        }
    }
    //TODO : i18n 준비
    @Composable
    fun PermissionRequestPage(
        essential: List<PermissionDataModel>,
        optional: List<PermissionDataModel>
    ) {
        val permissionAllowed by permissionAllowedToExit.collectAsStateWithLifecycle()
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                var permissionRequestCount by remember { mutableIntStateOf(0) }
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(Modifier.height(60.dp))// 고정된 크기?
                    Text(
                        "앱 사용을 위해\n접근 권한을 허용해주세요.",
                        style = SlugTypographyStyle.HeadingSmallBold,
                        color = Gray700
                    )
                    Spacer(Modifier.height(40.dp))
                    PermissionList("필수 권한", essential)
                    if (optional.isNotEmpty() && essential.isNotEmpty()) {
                        Spacer(Modifier.height(32.dp))
                    }
                    PermissionList("선택 권한", optional)
                    Spacer(Modifier.height(40.dp))
                }
                Column(
                    modifier = Modifier
                        .background(color = Gray50)
                        .weight(1f)
                        .padding(all = 20.dp)
                )
                {
                    PermissionNotice(
                        name = "접근권한 안내",
                        description = "접근권한은 서비스 사용 중 필요한 시점에 동의를 받고 있습니다. 허용하지 않을 경우에도 해당 기능 외 서비스는 이용 할 수 있습니다."
                    )
                    Spacer(Modifier.height(24.dp))
                    PermissionNotice(
                        name = "접근 권한 변경안내",
                        description = "휴대폰 설정 > 앱 > 민달팽이"
                    )
                    Spacer(Modifier.weight(1f))
                    BasicButton(
                        buttonText = stringResource(
                            id = when {
                                permissionAllowed -> R.string.permission_confirm
                                permissionRequestCount > REQUEST_MAX_LIMIT -> R.string.permission_close_app
                                else -> R.string.permission_request
                            },
                        ),
                        onButtonClick = {
                            onBottomButtonClicked(permissionAllowed, permissionRequestCount)
                            permissionRequestCount++
                        }
                    )
                }

            }
        }
    }

    @Composable
    private fun PermissionNotice(name: String, description: String) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = name,
                style = SlugTypographyStyle.BodyMiniMedium,
                color = Gray700
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = SlugTypographyStyle.BodyMiniRegular,
                color = Gray400
            )
        }
    }

    private fun onBottomButtonClicked(permissionAllowed: Boolean, permissionRequestCount: Int) {
        when {
            permissionAllowed -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this@PermissionRequestActivity.finish()
            }

            permissionRequestCount == REQUEST_MAX_LIMIT -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = "package:$packageName".toUri()
                startActivity(intent)
            }

            permissionRequestCount > REQUEST_MAX_LIMIT -> this@PermissionRequestActivity.finishAffinity()
            else -> {
                permissionChecker.requestPermission()
            }
        }
    }

    @Composable
    fun PermissionList(type: String, datas: List<PermissionDataModel>) {
        if (datas.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    text = type,
                    style = SlugTypographyStyle.BodyMediumMedium
                )
                for (data in datas) {
                    PermissionElement(data)
                }
            }
        }
    }

    @Composable
    fun PermissionElement(permissionData: PermissionDataModel) {
        Row {
            Image(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(24.dp),
                painter = painterResource(id = permissionData.iconId),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row {
                Text(
                    text = stringResource(id = permissionData.permissionNameId),
                    style = SlugTypographyStyle.TitleMediumMedium,
                    color = Gray700
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = permissionData.permissionDescriptionId),
                    style = SlugTypographyStyle.BodySmallRegular,
                    color = Gray400TextSubtext
                )
            }

        }
    }

    @Preview
    @Composable
    fun PreViewPermissionElement() {
        Surface(modifier = Modifier.width(width = 222.dp)) {
            PermissionElement(PermissionDataModel.Location)
        }
    }

    @Preview(showBackground = true, backgroundColor = 0x00000000)
    @Composable
    fun PermissionRequestDialogPreview() {
        val essential: List<PermissionDataModel> = PermissionDataModel.essentialPermissions
        val optional = PermissionDataModel.optionalPermissions
        SlugTheme {
            PermissionRequestPage(
                essential = essential,
                optional = optional
            )
        }
    }
}
