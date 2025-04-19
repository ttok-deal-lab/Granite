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
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            sharedAppKeys()
        }
        release {
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
    val kakaoAppKey = getApiKey("KAKAO_APP_KEY")
    manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey
    addBuildConfigField("KAKAO_APP_KEY", "KAKAO_APP_KEY")
    addBuildConfigField("GOOGLE_APP_KEY", "GOOGLE_APP_KEY")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)


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