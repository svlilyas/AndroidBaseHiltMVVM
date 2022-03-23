// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Classpaths.gradleClasspath)
        classpath(Classpaths.kotlinGradleClasspath)
        classpath(Classpaths.kotlinSerialization)
        classpath(Classpaths.safeVarargs)
        classpath(Classpaths.hiltGradleClasspath)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        //google()
        //mavenCentral()
    }
}

tasks.register("clean").configure {
    delete("build")
}
