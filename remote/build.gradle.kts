plugins {
    kotlin("jvm") version "1.4.20"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "me.ali"
version = "0.1"

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Client.authBasic)
    implementation(Ktor.Client.core)
    implementation("io.ktor:ktor-client-jackson:1.4.1")
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")

    api(Ktor.Client.apache)

    testApi(Ktor.Client.jsonTests)
    testApi(Ktor.Client.mock)
    testApi(Testing.Kotest.Assertions.json)
    testApi(Testing.Kotest.Assertions.ktor)

    implementation(Ktor.Client.logging)
    implementation(Ktor.Client.json)
}
