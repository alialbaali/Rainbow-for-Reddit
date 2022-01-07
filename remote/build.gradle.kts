plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.30"
}

dependencies {
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Client.authBasic)
    implementation(Ktor.Client.core)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    api(Ktor.Client.cio)
    implementation(Ktor.Client.logging)
    implementation(Ktor.Client.json)

    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
    testApi(Ktor.Client.jsonTests)
    testApi(Ktor.Client.mock)
    testApi(Testing.Kotest.Assertions.json)
    testApi(Testing.Kotest.Assertions.ktor)
    api("com.russhwolf:multiplatform-settings:0.8")
    api("com.russhwolf:multiplatform-settings-no-arg:0.8")
    api("com.russhwolf:multiplatform-settings-coroutines:0.8")
}