package com.estateslug.slug.login.sns.sns

import com.estateslug.slug.login.sns.SocialLoginType

interface SocialLoginResultCallback {
    fun onSuccess(token: String, email: String, type: SocialLoginType)

    fun onFailure(exception: Throwable, type: SocialLoginType)
}
