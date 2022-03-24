include(":app")
include(":data")

rootProject.buildFileName = "build.gradle.kts"

enableFeaturePreview("VERSION_CATALOGS")

// Set single lock file (gradle.lockfile)
// This preview feature should be enabled by default in Gradle 7
// More: https://docs.gradle.org/current/userguide/dependency_locking.html#single_lock_file_per_project
enableFeaturePreview("ONE_LOCKFILE_PER_PROJECT")

// Gradle plugins are added via plugin management, not the classpath
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    // Using the plugins  DSL allows generating type-safe accessors for Kotlin DSL
    plugins {
        // See Dependency management section in README.md
        // https://github.com/igorwojda/android-showcase#dependency-management
        val agpVersion: String by settings
        id("com.android.application") version agpVersion

        val libraryVersion: String by settings
        id("com.android.library") version libraryVersion

        val kotlinVersion: String by settings
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion

        val navigationVersion: String by settings
        id("androidx.navigation.safeargs.kotlin") version navigationVersion

        val hiltVersion: String by settings
        id("dagger.hilt.android.plugin") version hiltVersion

        val detektVersion: String by settings
        id("io.gitlab.arturbosch.detekt") version detektVersion

        val ktlintGradleVersion: String by settings
        id("org.jlleitschuh.gradle.ktlint") version ktlintGradleVersion

        val androidJUnit5Version: String by settings
        id("de.mannodermaus.android-junit5") version androidJUnit5Version
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application",
                "com.android.library" -> {
                    val agpCoordinates: String by settings
                    useModule(agpCoordinates)
                }
                "androidx.navigation.safeargs.kotlin" -> {
                    val navigationCoordinates: String by settings
                    useModule(navigationCoordinates)
                }
                "de.mannodermaus.android-junit5" -> {
                    val androidJnit5Coordinates: String by settings
                    useModule(androidJnit5Coordinates) // navigationVersion
                }
                "dagger.hilt.android.plugin" -> {
                    useModule("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
                }
            }
        }
    }
}

// See Dependency management section in README.md
// https://github.com/igorwojda/android-showcase#dependency-management
@Suppress("detekt.StringLiteralDuplication")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            val kotlinVersion: String by settings
            version("kotlin", kotlinVersion)
            // Required by Android dynamic feature modules and SafeArgs
            alias("kotlin-reflect").to("org.jetbrains.kotlin", "kotlin-reflect")
                .versionRef("kotlin")
            version("coroutines", "1.+")
            alias("coroutines").to("org.jetbrains.kotlinx", "kotlinx-coroutines-android")
                .versionRef("coroutines")
            bundle("kotlin", listOf("kotlin-reflect", "coroutines"))

            version("retrofit", "2.+")
            alias("retrofit-core").to("com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            alias("converter-moshi").to("com.squareup.retrofit2", "converter-moshi")
                .versionRef("retrofit")
            bundle("retrofit", listOf("retrofit-core", "converter-moshi"))

            // Hilt
            version("hilt", "2.38.1")
            alias("hiltAndroid").to("com.google.dagger", "hilt-android").versionRef("hilt")
            alias("hiltCompiler").to("com.google.dagger", "hilt-android-compiler")
                .versionRef("hilt")
            bundle("hilt", listOf("hiltAndroid", "hiltCompiler"))

            // Retrofit will use okhttp 4 (it has binary capability with okhttp 3)
            // See: https://square.github.io/okhttp/upgrading_to_okhttp_4/
            version("okhttp", "4.+")
            alias("okhttp-okhttp").to("com.squareup.okhttp3", "okhttp").versionRef("okhttp")
            alias("okhttp-interceptor").to("com.squareup.okhttp3", "logging-interceptor")
                .versionRef("okhttp")
            // bundle is basically an alias for several dependencies
            bundle("okhttp", listOf("okhttp-okhttp", "okhttp-interceptor"))

            version("gson", "2.8.7")
            alias("gson-core").to("com.google.code.gson", "gson").versionRef("gson")

            version("gsonConvertor", "2.9.0")
            alias("gson-convertor").to("com.squareup.retrofit2", "converter-gson")
                .versionRef("gsonConvertor")
            // bundle is basically an alias for several dependencies
            bundle("gson", listOf("gson-core", "gson-convertor"))

            version("rxJava3", "2.9.0")
            alias("rxJava3Adapter").to("com.squareup.retrofit2", "adapter-rxjava3")
                .versionRef("rxJava3")

            version("scalar", "2.6.2")
            alias("scalarConverter").to("com.squareup.retrofit2", "converter-scalars")
                .versionRef("scalar")

            bundle(
                "network",
                listOf(
                    "gson-core",
                    "gson-convertor",
                    "rxJava3Adapter",
                    "scalarConverter",
                )
            )

            version(
                "stetho",
                "1.5.0"
            ) // 1.5.1 has critical bug and newer version is unlikely to be release
            alias("stetho-core").to("com.facebook.stetho", "stetho").versionRef("stetho")
            alias("stetho-okhttp3").to("com.facebook.stetho", "stetho-okhttp3").versionRef("stetho")
            bundle("stetho", listOf("stetho-core", "stetho-okhttp3"))

            version("kodein", "6.+")
            // Required by Android dynamic feature modules and SafeArgs
            alias("kodein-core").to("org.kodein.di", "kodein-di-generic-jvm").versionRef("kodein")
            alias("kodein-android-x").to("org.kodein.di", "kodein-di-framework-android-x")
                .versionRef("kodein")
            bundle("kodein", listOf("kodein-core", "kodein-android-x"))

            alias("timber").to("com.jakewharton.timber:timber:4.+")
            alias("constraintLayout").to("androidx.constraintlayout:constraintlayout:2.+")
            alias("coordinatorLayout").to("androidx.coordinatorlayout:coordinatorlayout:1.+")
            alias("appcompat").to("androidx.appcompat:appcompat:1.+")
            alias("recyclerview").to("androidx.recyclerview:recyclerview:1.+")
            alias("material").to("com.google.android.material:material:1.+")
            alias("lottie").to("com.airbnb.android:lottie:2.+")
            alias("coil").to("io.coil-kt:coil:1.+")
            alias("play-core").to("com.google.android.play:core:1.+")

            alias("core-ktx").to("androidx.core:core-ktx:1.+")
            alias("fragment-ktx").to("androidx.fragment:fragment-ktx:1.+")
            bundle("ktx", listOf("core-ktx", "fragment-ktx"))

            version("lifecycle", "2.+")
            alias("viewmodel-ktx").to("androidx.lifecycle", "lifecycle-viewmodel-ktx")
                .versionRef("lifecycle")
            alias("livedata-ktx").to("androidx.lifecycle", "lifecycle-livedata-ktx")
                .versionRef("lifecycle")
            alias("lifecycle-common").to("androidx.lifecycle", "lifecycle-common-java8")
                .versionRef("lifecycle")
            bundle("lifecycle", listOf("viewmodel-ktx", "livedata-ktx", "lifecycle-common"))

            val navigationVersion: String by settings
            version("navigation", navigationVersion)
            alias("navigation-fragment").to("androidx.navigation", "navigation-fragment-ktx")
                .versionRef("navigation")
            alias("navigation-dynamic")
                .to("androidx.navigation", "navigation-dynamic-features-fragment")
                .versionRef("navigation")
            alias("navigation-ui-ktx").to("androidx.navigation", "navigation-ui-ktx")
                .versionRef("navigation")
            bundle(
                "navigation",
                listOf("navigation-fragment", "navigation-dynamic", "navigation-ui-ktx")
            )

            version("room", "2.+")
            alias("room-ktx").to("androidx.room", "room-ktx").versionRef("room")
            alias("room-runtime").to("androidx.room", "room-runtime").versionRef("room")
            bundle("room", listOf("room-ktx", "room-runtime"))

            alias("room.compiler").to("androidx.room", "room-compiler").versionRef("room")

            // MultiDex
            version("multidex-lib", "2.0.1")
            alias("multidex").to("androidx.multidex", "multidex").versionRef("multidex-lib")

            // Glide
            version("glide", "4.12.0")
            alias("glide-core").to("com.github.bumptech.glide", "glide").versionRef("glide")
            alias("glide-okhttp3").to("com.github.bumptech.glide", "okhttp3-integration")
                .versionRef("glide")
            alias("glide-compiler").to("com.github.bumptech.glide", "compiler")
                .versionRef("glide")
            bundle("glide", listOf("glide-core", "glide-okhttp3", "glide-compiler"))

            // Test dependencies
            alias("test-coroutines").to("org.jetbrains.kotlinx", "kotlinx-coroutines-test")
                .versionRef("coroutines")

            version("kluent", "1.+")
            alias("kluent-android").to("org.amshove.kluent", "kluent-android").versionRef("kluent")

            alias("test-runner").to("androidx.test:runner:1.+")
            alias("espresso").to("androidx.test.espresso:espresso-core:3.+")
            alias("mockk").to("io.mockk:mockk:1.+")
            alias("arch").to("androidx.arch.core:core-testing:2.+")

            version("junit", "5.+")
            alias("junit-jupiter-api").to("org.junit.jupiter", "junit-jupiter-api")
                .versionRef("junit")

            bundle(
                "test",
                listOf(
                    "test-coroutines",
                    "kluent-android",
                    "test-runner",
                    "espresso",
                    "mockk",
                    "arch",
                    "junit-jupiter-api"
                )
            )

            alias("junit-jupiter-engine").to("org.junit.jupiter", "junit-jupiter-engine")
                .versionRef("junit")
        }
    }
}
