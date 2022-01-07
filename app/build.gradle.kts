import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.1"
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":data"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    implementation("com.alialbaali.kamel:kamel-image:0.3.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.4.0")
    implementation("com.arkivanov.decompose:decompose:0.4.0")
    implementation("uk.co.caprica:vlcj:4.7.1")
    implementation ("com.halilibo.compose-richtext:richtext-commonmark:0.10.0")
}

compose.desktop {
    application {
        mainClass = "com.rainbow.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Msi, TargetFormat.Exe)
        }
    }
}