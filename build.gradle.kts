// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.ANDROID_APPLICATION) version "7.2.2" apply false
    id(Plugins.ANDROID_LIBRARY) version "7.2.2" apply false
    kotlin(Plugins.ANDROID) version "1.7.10" apply false
    id(Plugins.GMS) version "4.3.13" apply false
    id(Plugins.CRASHLYTICS) version "2.9.2" apply false
    id(Plugins.DETEKT)
    id(Plugins.KTLINT_GRADLE)
    id(Plugins.ANDROID_JUNIT_5) apply false
    id(Plugins.SAFE_ARGS) apply false
    id(Plugins.DAGGER_HILT) apply false
}

// all projects = root project + sub projects
allprojects {
    repositories {
        google()
        mavenCentral()
    }

    // We want to apply ktlint at all project level because it also checks Gradle config files (*.kts)
    apply(plugin = Plugins.KTLINT_GRADLE)

    // Ktlint configuration for sub-projects
    ktlint {
        verbose.set(true)
        android.set(true)

        // Uncomment below line and run .\gradlew ktlintCheck to see check ktlint experimental rules
        // enableExperimentalRules.set(true)

        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }

        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }

    configurations.all {
        resolutionStrategy {
            // objenesis: 3.1 used in mockk 1.12.0 is causing UI tests crash
            // (gradlew :library_test_utils:mergeExtDexDebugAndroidTest), so older version has to be used
            // https://github.com/mockk/mockk/issues/281
            force("org.objenesis:objenesis:2.6")
        }
    }

    // Gradle dependency locking - lock all configurations of the app
    // More: https://docs.gradle.org/current/userguide/dependency_locking.html
    dependencyLocking {
        lockAllConfigurations()
    }
}

subprojects {
    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    }

    apply(plugin = Plugins.DETEKT)

    detekt {
        config = files("$rootDir/detekt.yml")

        parallel = true

        // By default detekt does not check test source set and gradle specific files, so hey have to be added manually
        input = files(
            "$rootDir/buildSrc",
            "$rootDir/build.gradle.kts",
            "$rootDir/settings.gradle.kts",
            "src/main/kotlin",
            "src/test/kotlin"
        )
    }

    afterEvaluate {
        configureAndroid()
    }

    // While writing versions locks pre-release version of dependencies will be ignored
    configurations.all {
        resolutionStrategy.componentSelection {
            // Accept the highest version matching the requested version that isn't...
            all {
                // detekt is using pre-release dependencies
                val detektExceptions = listOf(
                    "io.gitlab.arturbosch.detekt",
                    "com.fasterxml.jackson",
                    "com.fasterxml.jackson.core",
                    "com.fasterxml.jackson"
                )

                if (detektExceptions.any { it == candidate.group }) {
                    return@all
                }

                // android lint is using pre-release dependencies
                val androidLintExceptions = listOf("com.android.tools.build")

                if (androidLintExceptions.any { it == candidate.group }) {
                    return@all
                }
            }
        }
    }
}

fun Project.configureAndroid() {
    (project.extensions.findByName("android") as? com.android.build.gradle.BaseExtension)?.run {
        sourceSets {
            map { it.java.srcDir("src/${it.name}/kotlin") }
        }
    }
}

// Target version of the generated JVM bytecode. It is used for type resolution.
tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    this.jvmTarget = "1.8"
}

/*
Mimics all static checks that run on CI.
Note that this task is intended to run locally (not on CI), because on CI we prefer to have parallel execution
and separate reports for each of the checks (multiple statuses eg. on github PR page).
 */
task("staticCheck") {
    group = "verification"

    afterEvaluate {
        // Filter modules with "lintDebug" task (non-Android modules do not have lintDebug task)
        val lintTasks = subprojects.mapNotNull { "${it.name}:lintDebug" }

        // Get modules with "testDebugUnitTest" task (app module does not have it)
        val testTasks = subprojects.mapNotNull { "${it.name}:testDebugUnitTest" }
            .filter { it != "app:testDebugUnitTest" }

        // All task dependencies
        val taskDependencies =
            mutableListOf("app:assembleAndroidTest", "ktlintCheck", "detekt").also {
                it.addAll(lintTasks)
                it.addAll(testTasks)
            }

        // By defining Gradle dependency all dependent tasks will run before this "empty" task
        dependsOn(taskDependencies)
    }
}

tasks.register("clean").configure {
    delete("build")
}
