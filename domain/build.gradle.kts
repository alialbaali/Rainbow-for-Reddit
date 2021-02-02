plugins {
    kotlin("jvm") version "1.4.21"
}

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
    mavenCentral()
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
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    api(KotlinX.Coroutines.core)
    testApi("io.kotest:kotest-assertions-kotlinx-time:4.3.1")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
}
