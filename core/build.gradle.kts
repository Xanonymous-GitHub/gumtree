plugins {
    alias(libs.plugins.kotlinJvm)
    application
    antlr
}

group = "tw.xcc.gumtree"
version = "1.0.0"
application {
    mainClass.set("tw.xcc.gumtree.ApplicationKt")

    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    antlr(libs.antlr)
    implementation(libs.logback)

    // Test dependencies
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

internal val projectRootLocation: String = layout.projectDirectory.toString()
internal val forcedAntlrPackageName = "$group.model.lang"
internal val antlrGenFilesLocation =
    file(
        "$projectRootLocation/src/main/java/" +
            forcedAntlrPackageName.replace('.', '/')
    )

tasks {
    generateGrammarSource {
        outputDirectory = antlrGenFilesLocation
        arguments = listOf("-package", forcedAntlrPackageName)
    }

    test {
        useJUnitPlatform()
    }
}