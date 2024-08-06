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