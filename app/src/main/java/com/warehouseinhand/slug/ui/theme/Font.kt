package com.warehouseinhand.slug.ui.theme

import androidx.annotation.FontRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.warehouseinhand.slug.R


val pretendardVariableFonts = makeFontsForWeights(R.font.pretendard_variable)

val pretendardFontFamily = FontFamily(pretendardVariableFonts)

@OptIn(ExperimentalTextApi::class)
fun makeFontsForWeights(@FontRes fontId: Int) =
    (100..900 step 100).map { fontWeight ->
        Font(
            resId = fontId,
            variationSettings = FontVariation.Settings(
                FontVariation.Setting(name = "wght", value = fontWeight.toFloat())
            ),
            weight = FontWeight(fontWeight)
        )
    }

@Composable
@Preview(showSystemUi = true)
fun FontPreview() {
    val testText = "다람쥐 헌 챗바퀴 타고파"
    Column {
        for (weight in 100..1000 step 100)
            Text(
                text = testText,
                fontSize = 28.sp,
                fontFamily = pretendardFontFamily,
                fontWeight = FontWeight(weight)
            )
    }
}