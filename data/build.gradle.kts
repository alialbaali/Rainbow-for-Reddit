plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    implementation(Ktor.client.core)
    implementation(RussHWolf.MultiplatformSettings.settings)
    implementation(RussHWolf.MultiplatformSettings.noArg)
    implementation(RussHWolf.MultiplatformSettings.coroutines)
}