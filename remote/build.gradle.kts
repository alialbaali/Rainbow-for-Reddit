plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "me.ali"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Client.authBasic)
    implementation(Ktor.Client.core)
    implementation("io.ktor:ktor-client-jackson:1.4.1")
    api(Ktor.Client.apache)
    implementation(Ktor.Client.logging)
    implementation(Ktor.Client.json)
}
