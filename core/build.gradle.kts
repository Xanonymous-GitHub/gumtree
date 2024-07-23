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
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)
    implementation(libs.cryptoHash)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    test {
        useJUnitPlatform()
    }
}