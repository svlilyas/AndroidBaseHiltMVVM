import com.android.build.api.dsl.ApplicationProductFlavor

plugins {
    id(Plugins.ANDROID_LIBRARY)
    kotlin(Plugins.ANDROID)
    kotlin(Plugins.KAPT)
    kotlin(Plugins.ANDROID_EXTENSIONS)
    id(Plugins.DAGGER_HILT)
}

androidExtensions {
    isExperimental = true
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION

        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
    }

    sourceSets {
        getByName(Flavors.Default.MAIN) {
            java.srcDir("src/main/kotlin")
        }
    }

    buildTypes {
        getByName(Flavors.BuildTypes.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    kapt(libs.room.compiler)
    api(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    testImplementation(libs.bundles.test)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

fun ApplicationProductFlavor.stringField(entry: Pair<String, String>) {
    buildConfigField("String", entry.first, "\"${entry.second}\"")
}
