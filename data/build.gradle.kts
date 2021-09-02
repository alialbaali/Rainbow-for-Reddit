plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
    api("com.russhwolf:multiplatform-settings:0.8")
    api("com.russhwolf:multiplatform-settings-coroutines:0.8")
}
