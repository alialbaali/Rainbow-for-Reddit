plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.10"
    id("com.squareup.sqldelight")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    api("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.0")
}

sqldelight {
    database("RainbowDatabase") {
        packageName = "com.rainbow.sql"
    }
}