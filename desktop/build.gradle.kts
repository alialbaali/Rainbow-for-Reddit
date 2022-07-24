import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.1"
}

dependencies {
    implementation(project(":common"))
    implementation(compose.desktop.currentOs)
    api(compose.preview)
}

compose.desktop {
    application {
        mainClass = "com.rainbow.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}