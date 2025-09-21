package com.warehouseinhand.slug.login.sns

enum class SocialLoginType(val typeName: String) {
    NEVER_LOGIN(typeName = "NEVER_LOGIN"),
    NAVER(typeName = "NAVER"),
    KAKAO(typeName = "KAKAO"),
    GOOGLE(typeName = "GOOGLE"),
    APPLE(typeName = "APPLE"),
    ;

    companion object {
        fun fromTypeName(typeName: String): SocialLoginType =
            SocialLoginType.entries.find { it.typeName == typeName } ?: NEVER_LOGIN
    }
}
