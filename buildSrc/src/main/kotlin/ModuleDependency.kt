import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Features Management Class
 * Reaching all feature dependencies from this class
 */
object ModuleDependency {
    // Main Path
    const val PATH = "path"

    // Feature Paths
    const val FEATURE_APP = ":app"
    const val FEATURE_DATA = ":data"

    object Project {
        fun DependencyHandler.app(): Dependency = project(mapOf(PATH to FEATURE_APP))
        fun DependencyHandler.data(): Dependency = project(mapOf(PATH to FEATURE_DATA))
    }
}
