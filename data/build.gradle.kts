plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
    api("com.russhwolf:multiplatform-settings:0.9")
    api("com.russhwolf:multiplatform-settings-no-arg:0.9")
    api("com.russhwolf:multiplatform-settings-coroutines:0.9")
}
