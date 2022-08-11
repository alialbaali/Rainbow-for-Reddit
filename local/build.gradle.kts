plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.30"
    id("com.squareup.sqldelight")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    api("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation(project(":domain"))
}

sqldelight {
    database("RainbowDatabase") {
        packageName = "com.rainbow.sql"
    }
}