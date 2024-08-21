plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinxAtomicfu)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.kotlinxBenchmark)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.immutable)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)
    implementation(libs.kotlinx.benchmark)
    implementation(libs.cryptoHash)

    testImplementation(libs.kotlin.reflection)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

kover {
    reports {
        filters {
            excludes {
                classes("tw.xcc.gumtree.api.*")
            }
        }
    }
}

tasks.test {
    finalizedBy(tasks.koverXmlReport)
    finalizedBy(tasks.koverHtmlReport)
    finalizedBy(tasks.koverBinaryReport)
    finalizedBy(tasks.koverPrintCoverage)
}