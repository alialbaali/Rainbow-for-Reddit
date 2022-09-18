import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev774"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(KotlinX.Coroutines.swing)
    implementation("com.alialbaali.kamel:kamel-image:0.4.1")
    implementation("com.halilibo.compose-richtext:richtext-commonmark:0.13.0")
    implementation("com.halilibo.compose-richtext:richtext-ui-material:0.13.0")
    api(compose.preview)
    implementation(project(":domain"))
    implementation(project(":data"))
}

compose.desktop {
    application {
        mainClass = "com.rainbow.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            macOS {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.png"))
            }
            packageName = "Rainbow"
            packageVersion = "1.0.0"
        }
    }
}