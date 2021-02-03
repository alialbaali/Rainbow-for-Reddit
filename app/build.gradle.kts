import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "0.3.0-build148"
    application
}

repositories {
    mavenLocal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":data"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    implementation("io.kamel:kamel-core:0.0.4")
    implementation("com.arkivanov.decompose:decompose:0.1.7")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.1.7")
}

application {
    mainClass.set("com.rainbow.app.MainKt")
}