import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlinJvm) apply false
}

allprojects {
    val localJavaVersion = JavaVersion.VERSION_21.majorVersion

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(localJavaVersion))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(localJavaVersion.toInt())
    }
}