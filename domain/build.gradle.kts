plugins {
    kotlin("jvm")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
    api(KotlinX.Coroutines.core)
    testApi("io.kotest:kotest-assertions-kotlinx-time:4.4.3")
    testApi(Testing.Kotest.core)
    testApi(Testing.Kotest.Assertions.core)
    testApi(Testing.Kotest.property)
    testApi(Testing.Kotest.Runner.junit5)
}
