import com.android.build.api.dsl.ApplicationProductFlavor

// Application Specific Plugins
plugins {
    id(Plugins.ANDROID_APPLICATION)
    kotlin(Plugins.ANDROID)
    kotlin(Plugins.KAPT)
    id(Plugins.KOTLIN_EXTENSIONS)
}

androidExtensions {
    isExperimental = true
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

            buildConfigField("String", "sd", "\"llklk\"")
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
    // implementation(data())

    // Kotlin
    implementation(Dependencies.Kotlin.kotlinStdLib)
    implementation(Dependencies.Kotlin.kotlinCoroutinesCore)
    implementation(Dependencies.Kotlin.kotlinCoroutinesAndroid)

    // Android
    implementation(Dependencies.Android.androidCore)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.legacySupport)
    implementation(Dependencies.Android.multidex)
    implementation(Dependencies.Android.materialDesign)
    implementation(Dependencies.Android.fragment)
    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.recyclerView)
    implementation(Dependencies.Android.recyclerViewSelection)
    implementation(Dependencies.Android.cardView)
    implementation(Dependencies.Android.palette)
    implementation(Dependencies.Android.workManger)

    // Navigation
    implementation(Dependencies.Navigation.runTimeNavigation)
    implementation(Dependencies.Navigation.navigationFragment)
    implementation(Dependencies.Navigation.navigationUi)

    // LifeCycle
    implementation(Dependencies.LifeCycle.runTimeLifeCycle)
    implementation(Dependencies.LifeCycle.lifeCycleCompiler)
    implementation(Dependencies.LifeCycle.liveData)
    implementation(Dependencies.LifeCycle.viewModel)
    implementation(Dependencies.LifeCycle.viewModelState)

    // DI
    implementation(Dependencies.DI.hilt)
    kapt(Dependencies.DI.hiltCompiler)

    // ReactiveFunc
    implementation(Dependencies.ReactiveFunc.rxJava)
    implementation(Dependencies.ReactiveFunc.rxKotlin)
    implementation(Dependencies.ReactiveFunc.rxAndroid)
    implementation(Dependencies.Network.scalar)
    implementation(Dependencies.Network.gson)

    // AppCenter
    implementation(Dependencies.AppCenter.crashes)
    implementation(Dependencies.AppCenter.analytics)
    // Shimmer Layout
    implementation(Dependencies.Tools.shimmerLayout)

    // Timber
    implementation(Dependencies.Tools.timber)

    // ZoomageView
    implementation(Dependencies.Tools.zoomageView)

    // balloon
    implementation(Dependencies.Tools.balloon)

    // EventBus
    implementation(Dependencies.Tools.eventbus)

    // Lottie (Animation)
    implementation(Dependencies.Tools.lottie)

    // RoundedImageView
    implementation(Dependencies.Tools.roundedImageView)

    // LinkBuilder
    implementation(Dependencies.Tools.linkBuilder)

    // ViewPager2
    implementation(Dependencies.Tools.viewPager2)

    // WarmDotsIndicator
    implementation(Dependencies.Tools.dotsIndicator)

    // Dengage
    // implementation(Dependencies.Dengage.dengage)

    // Gson
    implementation(Dependencies.Tools.gson)

    // Glide
    implementation(Dependencies.Tools.glide)
    implementation(Dependencies.Tools.glideOkHttpIntegration)
    kapt(Dependencies.Tools.glideCompiler)

    // KeyboardListener
    implementation(Dependencies.Tools.keyboardListener)

    // Huawei

    // Google

    // Adjust
    implementation(Dependencies.Adjust.adjustAndroid)
    implementation(Dependencies.Adjust.identifier)
    implementation(Dependencies.Adjust.installreferrer)
    implementation(Dependencies.Adjust.webbridge)

    // Firebase

    // Dialog
    implementation(Dependencies.MaterialDialog.core)
    implementation(Dependencies.MaterialDialog.datetime)
    implementation(Dependencies.MaterialDialog.input)
    implementation(Dependencies.MaterialDialog.lifecycle)

    // Testing
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.truthExt)
    testImplementation(Dependencies.Test.mockK)
    testImplementation(Dependencies.Test.coreTesting)
    androidTestImplementation(Dependencies.Test.androidJunit)
    androidTestImplementation(Dependencies.Test.espressoCore)
}

fun ApplicationProductFlavor.stringField(entry: Pair<String, String>) {
    buildConfigField("String", entry.first, "\"${entry.second}\"")
}
