plugins {
    kotlin("jvm") version "1.4.21"
}

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}
kotlin {
    target.compilations.all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xallow-result-return-type"
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    api(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":local"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
}
