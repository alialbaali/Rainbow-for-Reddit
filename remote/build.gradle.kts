plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.30"
}

dependencies {
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Client.auth)
    implementation(Ktor.Client.core)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
    api(Ktor.Client.cio)
    implementation(Ktor.Client.logging)
    implementation(Ktor.Client.json)

    testApi(Ktor.Client.jsonTests)
    testApi(Ktor.Client.mock)
    api("com.russhwolf:multiplatform-settings:0.9")
    api("com.russhwolf:multiplatform-settings-no-arg:0.9")
    api("com.russhwolf:multiplatform-settings-coroutines:0.9")
}