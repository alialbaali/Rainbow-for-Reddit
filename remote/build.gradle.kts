plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.10"
}

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

kotlin {
    target.compilations.all {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xallow-result-return-type")
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(Ktor.Client.serialization)
    implementation(Ktor.Client.authBasic)
    implementation(Ktor.Client.core)
    implementation("io.ktor:ktor-client-jackson:1.4.1")
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    api(Ktor.Client.apache)
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
}