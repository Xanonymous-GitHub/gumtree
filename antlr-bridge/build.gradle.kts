plugins {
    alias(libs.plugins.kotlinxKover)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.antlr)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.immutable)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)

    testImplementation(libs.kotlin.reflection)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks.test {
    finalizedBy(tasks.koverXmlReport)
    finalizedBy(tasks.koverHtmlReport)
    finalizedBy(tasks.koverBinaryReport)
    finalizedBy(tasks.koverPrintCoverage)
}