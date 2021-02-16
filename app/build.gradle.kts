import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "0.3.0-build154"
}

repositories {
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":data"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    implementation("com.alialbaali.kamel:kamel-image:0.1.0")
    implementation("com.arkivanov.decompose:decompose:0.1.8")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.1.8")
}

compose.desktop {
    application {
        mainClass = "com.rainbow.app.MainKt"
    }
}