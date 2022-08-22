import com.android.build.api.dsl.LibraryBuildType

plugins {
    id(Plugins.ANDROID_LIBRARY)
    kotlin(Plugins.ANDROID)
    kotlin(Plugins.KAPT)
    id(Plugins.DAGGER_HILT)
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION

        version = AndroidConfig.VERSION_NAME

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

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }
    }

    buildTypes {
        getByName(Flavors.BuildTypes.DEBUG) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(libs.bundles.room)
    implementation(libs.hilt.android)
    implementation(libs.bundles.okhttp)
    implementation(libs.sandwich)
    implementation(libs.bundles.moshi)
    implementation(libs.timber)
    implementation(libs.bundles.retrofit)
    kapt(libs.hilt.compiler)
    kapt(libs.room.compiler)

    testImplementation(libs.bundles.test)
}

fun LibraryBuildType.stringField(entry: Pair<String, String>) {
    buildConfigField("String", entry.first, "\"${entry.second}\"")
}
