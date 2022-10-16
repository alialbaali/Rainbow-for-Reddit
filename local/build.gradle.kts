plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.7.10"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(project(":domain"))
}