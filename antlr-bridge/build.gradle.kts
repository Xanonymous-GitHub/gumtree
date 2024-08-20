plugins {
    jacoco
}

dependencies {
    implementation(project(":core"))
    implementation(libs.antlr)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflection)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = true
        html.required = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}