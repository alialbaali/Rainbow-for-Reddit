plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.10"
}

dependencies {
    val exposedVersion = "0.29.1"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:3.30.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}
