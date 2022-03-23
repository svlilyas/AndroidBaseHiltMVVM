import kotlin.reflect.full.memberProperties

// "Module" means "subproject" in terminology of Gradle API.
// To be specific each "Android module" is a Gradle "subproject"
@Suppress("unused")
object ModuleDependency {
    // All consts are accessed via reflection
    const val APP = ":app"
    const val FEATURE_DATA = ":data"

    // False positive" function can be private"
    // See: https://youtrack.jetbrains.com/issue/KT-33610
    /*
    Return list of all modules in the project
     */
    private fun getAllModules() = ModuleDependency::class.memberProperties
        .filter { it.isConst }
        .map { it.getter.call().toString() }
        .toSet()

    /*
     Return list of feature modules in the project
     */
    fun getFeatureModules(): Set<String> {
        val featurePrefix = ""

        return getAllModules()
            .filter { it.startsWith(featurePrefix) }
            .toSet()
    }
}
