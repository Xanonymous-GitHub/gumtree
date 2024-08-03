import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.gradleKtlint)
}

internal val kotlinJvmPluginId = libs.plugins.kotlinJvm.get().pluginId
internal val gradleKtLintPluginId = libs.plugins.gradleKtlint.get().pluginId

internal val localJavaVersion = JavaVersion.VERSION_21.majorVersion
internal val toolChainVersionValue = localJavaVersion.toInt()

allprojects {
    group = "tw.xcc.gumtree"
    version = "0.1.0"

    apply {
        plugin(kotlinJvmPluginId)
        plugin(gradleKtLintPluginId)
    }

    kotlin { jvmToolchain(toolChainVersionValue) }

    tasks.withType<KotlinCompile>().configureEach {
        dependsOn(tasks.ktlintFormat)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(localJavaVersion))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(toolChainVersionValue)
    }

    tasks.withType(Test::class) {
        useJUnitPlatform()
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
}