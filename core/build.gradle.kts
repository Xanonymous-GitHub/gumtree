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

    // Test dependencies
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    generateGrammarSource {
        // Since this project uses the ANTLR only for its interfaces, we don't need to generate the sources.
        enabled = false
    }
    generateTestGrammarSource {
        // Since this project uses the ANTLR only for its interfaces, we don't need to generate the sources.
        enabled = false
    }

    test {
        useJUnitPlatform()
    }
}