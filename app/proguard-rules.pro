# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Crashlytics 스택트레이스 해석용 — 라인 번호 유지, 원본 파일명은 은닉
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

#차후 모듈별 변경 필요.
-keep class com.estateslug.slug.data.**.*DTO { *; }

# *DTO 접미사가 아닌 Gson 모델(요청 바디 등) — @SerializedName 필드가 shrink로 제거되지 않게 유지
-keepclassmembers class com.estateslug.slug.data.network.** {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.estateslug.slug.data.network.user.UserPublicService$IdTokenRequest { *; }

# 서버 문자열과 매칭되는 enum — Gson 역직렬화(SalesCategory)·@Query name 전송(Region/BuildType/Sort 등)·
# valueOf 복원(DeepLinkTab)이 전부 상수명에 의존하므로 우리 패키지 enum 상수명 유지
-keepclassmembers enum com.estateslug.slug.** { *; }

# Retrofit — R8 full mode(AGP 8+) 보완 규칙: suspend/제네릭 시그니처 유지
-keepattributes Signature, InnerClasses, EnclosingMethod, AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#Google Login
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** { *;}
-keep class com.google.googlesignin.** { *; }
-keepnames class com.google.googlesignin.* { *; }
-keep class com.google.android.gms.auth.** { *; }

#Kakao Login
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

#Kakao maps
-keep class com.kakao.vectormap.** { *; }
-keep interface com.kakao.vectormap.**

-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**