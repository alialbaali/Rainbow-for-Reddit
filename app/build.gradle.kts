import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":data"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    implementation("com.alialbaali.kamel:kamel-image:0.3.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.4.0")
    implementation("com.arkivanov.decompose:decompose:0.4.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("uk.co.caprica:vlcj:4.7.0")
}

compose.desktop {
    application {
        mainClass = "com.rainbow.app.MainKt"
    }
}