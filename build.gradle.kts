import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.gradleKtlint)
}

ktlint {
    filter {
        exclude("**/buildSrc/**")
        exclude("**/build/**")
        exclude("**/generated/**")
        exclude("**/generated-src/**")
        include("**/kotlin/**")
        include("**/*.kts")
    }
}

internal val gradleKtLintPluginId = libs.plugins.gradleKtlint.get().pluginId

allprojects {
    apply(plugin = gradleKtLintPluginId)

    val localJavaVersion = JavaVersion.VERSION_21.majorVersion

    tasks.withType<KotlinCompile>().configureEach {
        dependsOn(tasks.ktlintFormat)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(localJavaVersion))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(localJavaVersion.toInt())
    }
}