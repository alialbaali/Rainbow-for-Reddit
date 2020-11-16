plugins {
    kotlin("jvm") version "1.4.10"
}

version = "unspecified"

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
}
