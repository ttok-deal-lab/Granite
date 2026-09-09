import com.android.build.api.dsl.VariantDimension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.oss.licenses)
    alias(libs.plugins.ksp)
    alias(libs.plugins.stability.analyzer)
}

// local.properties — AGP 내부 API(gradleLocalProperties) 대신 Provider API로 읽는다 (구성 캐시 입력으로 추적됨).
// 파일 상단에 두는 이유: android {} 블록이 구성 단계에서 즉시 호출하므로 먼저 초기화돼야 한다
val localProperties: Properties = Properties().apply {
    providers.fileContents(rootProject.layout.projectDirectory.file("local.properties"))
        .asText.orNull?.let { load(it.reader()) }
}

android {
    namespace = "com.estateslug.slug"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.estateslug.slug"
        minSdk = 28
        targetSdk = 37
        versionCode = 11
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // 카카오맵 네이티브(libK3fAndroid.so)가 ARM만 제공 — androidx 소형 립이 x86_64 폴더를
            // 만들면 Play가 x86_64 네이티브 지원으로 오판해 x86 기기에서 시작 크래시 발생.
            // ARM 전용으로 잘라 순수 x86 기기는 비호환 처리, ARM 변환 기기는 전체 변환 실행되게 한다
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
    }

    signingConfigs {
        // 자격 증명은 local.properties (커밋 금지). 4종이 모두 있을 때만 구성 —
        // 조건 없이 getApiKey를 부르면 설정 단계에서 debug 빌드까지 깨진다
        if (hasReleaseSigningKeys()) {
            create("release") {
                storeFile = file(getApiKey("KEYSTORE_FILE"))
                storePassword = getApiKey("KEYSTORE_PASSWORD")
                keyAlias = getApiKey("KEY_ALIAS")
                keyPassword = getApiKey("KEY_PASSWORD")
            }
        }
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
            // dev 서버 — 기존 local.properties 키 그대로 사용
            addBuildConfigField("BASE_URL")
        }
        release {
            if (hasReleaseSigningKeys()) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                logger.warn("⚠ release 서명 미구성 — local.properties에 KEYSTORE_FILE/KEYSTORE_PASSWORD/KEY_ALIAS/KEY_PASSWORD 필요 (산출물이 unsigned)")
            }
            resValue("string", "app_name", "민달팽이")//TODO : 나중에 수정
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            sharedAppKeys()
            // 프로덕션 서버 — local.properties의 BASE_URL_RELEASE 필요 (없으면 빌드 실패)
            addBuildConfigField("BASE_URL", "BASE_URL_RELEASE")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    // 빌트인 Kotlin: jvmTarget은 targetCompatibility(21)를 그대로 따르므로 별도 지정 불필요
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true // AGP 9.0부터 기본 false — buildTypes의 resValue("app_name") 사용에 필요
    }
}


fun hasReleaseSigningKeys(): Boolean =
    listOf("KEYSTORE_FILE", "KEYSTORE_PASSWORD", "KEY_ALIAS", "KEY_PASSWORD")
        .all { localProperties.getProperty(it) != null }

fun getApiKey(propertyKey: String): String {
    return localProperties.getProperty(propertyKey)
        ?: error("local.properties에 '$propertyKey'가 없습니다. AGENT_ANDROID.md의 필수 키 목록을 확인하세요.")
}

fun VariantDimension.addBuildConfigField(name: String, propertyKey: String = name) {
    buildConfigField("String", name, getApiKey(propertyKey))
}

// BASE_URL은 buildType별로 분리 — 각 buildTypes 블록에서 선언
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
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    //image
    implementation(libs.landscapist.image)
    implementation(libs.landscapist.placeholder)
    implementation(libs.landscapist.zoomable)


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

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)
    implementation(libs.firebase.config)

    implementation(libs.firebase.auth)//SNS FOR APPLE

    //SNS
    implementation(libs.naver.login)
    implementation(libs.google.googleid)
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.play.services.auth)
    implementation(libs.kakao.all)
//    implementation(libs.kakao.login)
    //Share
//    implementation(libs.kakao.share)
//    Map sdk
    implementation(libs.kakao.maps)


    //hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Opensource
    implementation(libs.google.play.services.oss.licenses)

}