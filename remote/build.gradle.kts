plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.10"
}

dependencies {
    implementation(Ktor.Client.auth)
    implementation(Ktor.Client.contentNegotiation)
    implementation(Ktor.Client.core)
    implementation(Ktor.Client.cio)
    implementation(Ktor.Client.logging)
    implementation(Ktor.Client.json)
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Plugins.Serialization.Kotlinx.json)
    implementation(KotlinX.datetime)
    implementation(RussHWolf.MultiplatformSettings.settings)
    implementation(RussHWolf.MultiplatformSettings.noArg)
    implementation(RussHWolf.MultiplatformSettings.coroutines)
}