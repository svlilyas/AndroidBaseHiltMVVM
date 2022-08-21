import ModuleDependency.Project.data
import com.android.build.api.dsl.ApplicationProductFlavor

// Application Specific Plugins
plugins {
    id(Plugins.ANDROID_APPLICATION)
    kotlin(Plugins.ANDROID)
    kotlin(Plugins.KAPT)
    id(Plugins.SAFE_ARGS)
    id(Plugins.DAGGER_HILT)
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    ndkVersion = AndroidConfig.NDK_VERSION

    defaultConfig {
        applicationId = AndroidConfig.APP_ID
        minSdk = AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION

        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        multiDexEnabled = true
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
        setProperty("archivesBaseName", "${AndroidConfig.APP_NAME}-$versionName($versionCode)")
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
            versionNameSuffix = "_${Flavors.ProductFlavors.DEV}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )
            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // uat
        create(Flavors.ProductFlavors.UAT) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ".${Flavors.ProductFlavors.UAT}"
            versionNameSuffix = "_${Flavors.ProductFlavors.UAT}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )
            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // pilot
        create(Flavors.ProductFlavors.PILOT) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ".${Flavors.ProductFlavors.PILOT}"
            versionNameSuffix = "_${Flavors.ProductFlavors.PILOT}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
            stringField(Fields.SERVICE_CERTIFICATE_PATH to "")
        }

        // store
        create(Flavors.ProductFlavors.STORE) {
            dimension = Flavors.FlavorDimensions.ENVIRONMENT
            applicationIdSuffix = ""
            versionNameSuffix = "_${Flavors.ProductFlavors.STORE}"

            resValue(
                "string",
                "app_label_name",
                "${AndroidConfig.APP_NAME}$versionNameSuffix"
            )

            // BuildConfigField
            stringField(Fields.SERVICE_URL to "https://api.openweathermap.org/data/")
            stringField(Fields.SERVICE_API_KEY to "")
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
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
    // Data Module
    implementation(data())

    // Gradle 7 introduces version catalogs - a new way for sharing dependency versions across projects.
    // Dependencies are defined in gradle.settings.kts file.
    // Code completion problem is fixed in InteliJ IDEA 2021.2 EAP 1 https://youtrack.jetbrains.com/issue/IDEA-266509
    api(libs.bundles.kotlin)
    api(libs.bundles.stetho)
    api(libs.bundles.retrofit)
    api(libs.bundles.okhttp)
    api(libs.play.core)
    api(libs.bundles.ktx)
    api(libs.bundles.navigation)
    api(libs.bundles.lifecycle)
    api(libs.bundles.room)
    api(libs.timber)
    api(libs.coil)
    api(libs.constraintLayout)
    api(libs.coordinatorLayout)
    api(libs.appcompat)
    api(libs.recyclerview)
    api(libs.material)
    api(libs.coroutines)
    api(libs.lottie)
    api(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    api(libs.multidex)
    api(libs.bundles.glide)

    kapt(libs.room.compiler)

    testImplementation(libs.bundles.test)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

fun ApplicationProductFlavor.stringField(entry: Pair<String, String>) {
    buildConfigField("String", entry.first, "\"${entry.second}\"")
}
