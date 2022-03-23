import java.util.*

object AndroidConfig {
    const val APP_NAME = "AndroidBaseHiltMVVM"
    const val APP_ID = "com.pi.androidbasehiltmvvm"
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31
    const val BUILD_TOOLS_VERSION = "30.0.3"
    const val COMPILE_SDK_VERSION = 31
    const val NDK_VERSION = "23.0.7599858"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    const val VERSION_CODE = 1
    val VERSION_NAME = calculateVersionName()
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    private fun calculateVersionName(): String = "v$versionMajor.$versionMinor.$versionPatch"
}

object Flavors {
    object ProductFlavors {
        const val DEV = "dev"
        const val UAT = "uat"
        const val PILOT = "pilot"
        const val STORE = "store"
    }

    object FlavorDimensions {
        const val ENVIRONMENT = "environment"
    }

    object BuildTypes {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    object Default {
        const val MAIN = "main"
        private const val BUILD_TYPE = BuildTypes.DEBUG
        private const val BUILD_FLAVOR = ProductFlavors.DEV

        val BUILD_VARIANT =
            "${BUILD_FLAVOR.capitalize(Locale.ROOT)}${BUILD_TYPE.capitalize(Locale.ROOT)}"
    }
}
