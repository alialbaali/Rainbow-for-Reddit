plugins {
    kotlin("jvm") version "1.4.20"
}

version = "unspecified"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
    mavenCentral()
}

kotlin {
    target.compilations.all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xallow-result-return-type"
        }
    }
}
dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    api(KotlinX.Coroutines.core)
    testApi("io.kotest:kotest-assertions-kotlinx-time:4.3.1")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
}
