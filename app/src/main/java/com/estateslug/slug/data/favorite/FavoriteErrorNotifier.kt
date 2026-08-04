package com.estateslug.slug.data.favorite

import android.content.Context
import android.widget.Toast
import com.estateslug.slug.R
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** 관심 토글 서버 반영 실패를 사용자에게 알린다. Toast를 스토어에서 분리해 JVM 단위 테스트를 가능하게 한다. */
interface FavoriteErrorNotifier {
    suspend fun notifyFavoriteActionFailed()
}

class ToastFavoriteErrorNotifier @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : FavoriteErrorNotifier {
    override suspend fun notifyFavoriteActionFailed() {
        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, R.string.favorite_action_failed_toast, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteErrorNotifierModule {
    @Binds
    abstract fun bindFavoriteErrorNotifier(impl: ToastFavoriteErrorNotifier): FavoriteErrorNotifier
}
