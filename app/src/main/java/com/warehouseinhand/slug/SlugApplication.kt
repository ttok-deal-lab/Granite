package com.warehouseinhand.slug

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SlugApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Kakao login init
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        //Kakao maps init
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY);
    }
}