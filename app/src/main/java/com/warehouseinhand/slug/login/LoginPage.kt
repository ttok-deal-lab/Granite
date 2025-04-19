package com.warehouseinhand.slug.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.login.SocialLoginButton
import com.warehouseinhand.slug.ui.theme.PrimaryBlack


@Composable
fun LoginPage() {
    Surface {
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = CircleShape)
                        .border(1.dp, color = PrimaryBlack, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("(서비스 로고)")
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.login_sub_title)
                )

            }
            SocialLoginButtons()
        }

    }
}

@Composable
private fun SocialLoginButtons() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SocialLoginUIModel.entries.forEach {
            SocialLoginButton(it) { }
        }
    }
}

@Composable
@Preview
fun PreviewLoginPage() {
    LoginPage()
}