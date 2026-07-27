package com.estateslug.slug

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.AuthEventBus
import com.estateslug.slug.login.LogInActivity
import com.estateslug.slug.permission.PermissionRequestActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltAndroidApp
class SlugApplication : Application() {

    @Inject
    lateinit var authEventBus: AuthEventBus

    @Inject
    lateinit var localUserDataRepository: LocalUserDataRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var currentActivityRef: WeakReference<Activity> = WeakReference(null)
    private var isHandlingExpiry = false

    override fun onCreate() {
        super.onCreate()
        //Kakao login init
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        //Kakao maps init
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        registerActivityLifecycleCallbacks(activityCallbacks)
        observeSessionExpired()
    }

    private val activityCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            currentActivityRef = WeakReference(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            if (currentActivityRef.get() === activity) {
                currentActivityRef = WeakReference(null)
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
        override fun onActivityStarted(activity: Activity) = Unit
        override fun onActivityStopped(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
    }

    private fun observeSessionExpired() {
        appScope.launch {
            authEventBus.events.collect {
                handleSessionExpired()
            }
        }
    }

    private suspend fun handleSessionExpired() {
        if (isHandlingExpiry) return
        isHandlingExpiry = true

        withContext(Dispatchers.IO) {
            localUserDataRepository.removeAllData()
        }

        val activity = currentActivityRef.get()
        Toast.makeText(
            activity ?: this,
            "세션이 만료되었습니다. 다시 로그인해주세요.",
            Toast.LENGTH_LONG,
        ).show()

        // 권한 인트로 화면이 떠 있으면 리다이렉트 생략 — 확인 시 아래의 로그인 화면으로 자연 복귀
        if (activity !is LogInActivity && activity !is PermissionRequestActivity) {
            val intent = Intent(this, LogInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            activity?.finish()
        }

        delay(RESET_GUARD_DELAY_MS)
        isHandlingExpiry = false
    }

    companion object {
        private const val RESET_GUARD_DELAY_MS = 2000L
    }
}
