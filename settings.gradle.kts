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
        /* val agpVersion: String by settings
         id("com.android.application") version agpVersion*/

        /* val libraryVersion: String by settings
         id("com.android.library") version libraryVersion*/

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
}
