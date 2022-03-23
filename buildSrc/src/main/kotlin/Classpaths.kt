object Classpaths {
    const val gradleClasspath =
        "com.android.tools.build:gradle:${Versions.Gradle.gradleVersion}"
    const val kotlinGradleClasspath =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.kotlinVersion}"
    const val safeVarargs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.Gradle.safeArgs}"
    const val gradleVersionPlugin =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.Gradle.gradleVersionPluginVersion}"
    const val hiltGradleClasspath =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.Gradle.hiltVersion}"
    const val huaweiPlugin =
        "com.huawei.agconnect:agcp:${Versions.Huawei.huaweiPlugin}"
    const val googleService =
        "com.google.gms:google-services:${Versions.Google.googleService}"
    const val firebaseCrashPlugin =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.Firebase.firebaseCrashPlugin}"

    const val perfPlugin =
        "com.google.firebase:perf-plugin:${Versions.Firebase.perfPlugin}"

    const val kotlinSerialization =
        "org.jetbrains.kotlin:kotlin-serialization:${Versions.Kotlin.kotlinVersion}"
}
