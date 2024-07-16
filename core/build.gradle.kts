plugins {
    alias(libs.plugins.kotlinJvm)
    application
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
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    test {
        useJUnitPlatform()
    }
}