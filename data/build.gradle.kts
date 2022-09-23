plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    implementation(Ktor.client.core)
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
    api("com.russhwolf:multiplatform-settings:0.9")
    api("com.russhwolf:multiplatform-settings-no-arg:0.9")
    api("com.russhwolf:multiplatform-settings-coroutines:0.9")
}
