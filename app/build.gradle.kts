import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.0-beta1"
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
}

compose.desktop {
    application {
        mainClass = "com.rainbow.app.MainKt"
    }
}