import com.android.build.api.dsl.VariantDimension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.warehouseinhand.slug"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.warehouseinhand.slug"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://REDACTED_BASE_URL/\"")
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "민달팽이_DEV")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            sharedAppKeys()
        }
        release {
            resValue("string", "app_name", "민달팽이")//TODO : 나중에 수정
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            sharedAppKeys()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}


fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

fun VariantDimension.addBuildConfigField(name: String, propertyKey: String = name) {
    buildConfigField("String", name, getApiKey(propertyKey))
}

fun VariantDimension.sharedAppKeys() {
    manifestPlaceholders["KAKAO_APP_KEY"] = getApiKey("KAKAO_APP_KEY_MANIFEST")
    addBuildConfigField("KAKAO_APP_KEY", "KAKAO_APP_KEY")
    addBuildConfigField("APPLE_CLIENT_ID", "APPLE_CLIENT_ID")
    addBuildConfigField("GOOGLE_APP_KEY", "GOOGLE_APP_KEY")
    addBuildConfigField("NAVER_CLIENT_ID", "NAVER_CLIENT_ID")
    addBuildConfigField("NAVER_CLIENT_SECRET", "NAVER_CLIENT_SECRET")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    //accompanist
    implementation(libs.accompanist.webview)
    //image
    implementation(libs.glide.compose)


    //Network
    // Retrofit2
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp.logging)

    //Local
    //Data
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)

    implementation(libs.firebase.auth)//SNS FOR APPLE

    //SNS
    implementation(libs.naver.login)
    implementation(libs.kakao.login)

    implementation(libs.google.googleid)
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.play.services.auth)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

}