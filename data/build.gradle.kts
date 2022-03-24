import com.android.build.api.dsl.ApplicationProductFlavor

plugins {
    id(Plugins.ANDROID_APPLICATION)
    kotlin(Plugins.ANDROID)
    kotlin(Plugins.KAPT)
    id(Plugins.KOTLIN_EXTENSIONS)
    id(Plugins.DAGGER_HILT)
}

androidExtensions {
    isExperimental = true
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    ndkVersion = AndroidConfig.NDK_VERSION

    defaultConfig {
        // applicationId = AndroidConfig.APP_ID
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION

        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        multiDexEnabled = true
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
    }

    lint {
        this.abortOnError = true
        this.checkReleaseBuilds = true
        this.disable.add("NullSafeMutableLiveData")
    }
    signingConfigs {
        /*register(Flavors.BuildTypes.RELEASE) {
            storeFile = file(KeyStore.storeFilePath)
            storePassword = KeyStore.storePassword
            keyAlias = KeyStore.keyAlias
            keyPassword = KeyStore.keyPassword
        }*/
    }

    buildTypes {
        getByName(Flavors.BuildTypes.DEBUG) {
            isTestCoverageEnabled = true
            isMinifyEnabled = false
            isDebuggable = true
            // applicationIdSuffix = ".${Flavors.BuildTypes.DEBUG}"
            // signingConfig = signingConfigs.getByName(Flavors.BuildTypes.RELEASE)
        }

        getByName(Flavors.BuildTypes.RELEASE) {
            isMinifyEnabled = true
            isDebuggable = false
            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true
            defaultPublishConfig = name
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // signingConfig = signingConfigs.getByName(Flavors.BuildTypes.RELEASE)
        }
    }

    flavorDimensionList.add(Flavors.FlavorDimensions.ENVIRONMENT)
    productFlavors {
        // dev
        create(Flavors.ProductFlavors.DEV) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ".${Flavors.ProductFlavors.DEV}"
            versionNameSuffix = "_${Flavors.ProductFlavors.DEV}_${AndroidConfig.VERSION_NAME}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )
            // BuildConfigField
            stringField(Fields.SERVICE_URL to "")
            stringField(Fields.SERVICE_PUBLIC_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // uat
        create(Flavors.ProductFlavors.UAT) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ".${Flavors.ProductFlavors.UAT}"
            versionNameSuffix = "_${Flavors.ProductFlavors.UAT}_${AndroidConfig.VERSION_NAME}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )
            // BuildConfigField
            stringField(Fields.SERVICE_URL to "")
            stringField(Fields.SERVICE_PUBLIC_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // pilot
        create(Flavors.ProductFlavors.PILOT) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ".${Flavors.ProductFlavors.PILOT}"
            versionNameSuffix = "_${Flavors.ProductFlavors.PILOT}_${AndroidConfig.VERSION_NAME}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "")
            stringField(Fields.SERVICE_PUBLIC_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // store
        create(Flavors.ProductFlavors.STORE) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ""
            versionNameSuffix = "_${Flavors.ProductFlavors.STORE}_${AndroidConfig.VERSION_NAME}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "")
            stringField(Fields.SERVICE_PUBLIC_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }
    }

    sourceSets {
        getByName(Flavors.Default.MAIN) {
            java.srcDir("src/main/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/DEPENDENCIES")
            add("META-INF/LICENSE")
            add("META-INF/LICENSE.txt")
            add("META-INF/license.txt")
            add("META-INF/NOTICE")
            add("META-INF/NOTICE.txt")
            add("META-INF/notice.txt")
            add("META-INF/ASL2.0")
            add("META-INF/*.kotlin_module")
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
