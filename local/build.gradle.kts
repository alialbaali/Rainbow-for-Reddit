plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.30"
    id("com.squareup.sqldelight")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    api("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.1")
}

sqldelight {
    database("RainbowDatabase") {
        packageName = "com.rainbow.sql"
    }
}