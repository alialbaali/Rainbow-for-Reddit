import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.3.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)
    implementation(KotlinX.Coroutines.swing)
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation("com.alialbaali.kamel:kamel-image:0.4.1")
    implementation("com.halilibo.compose-richtext:richtext-commonmark:0.13.0")
    implementation("com.halilibo.compose-richtext:richtext-ui-material:0.13.0")
    implementation(project(":data"))
}

compose.desktop {
    application {
        mainClass = "com.rainbow.desktop.RainbowKt"
        nativeDistributions {
            targetFormats(
                TargetFormat.Deb,
//                TargetFormat.Rpm,
                TargetFormat.Dmg,
//                TargetFormat.Pkg,
                TargetFormat.Exe,
//                TargetFormat.Msi,
            )
            macOS {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/icons/Rainbow.png"))
            }
            packageName = "rainbow"
            packageVersion = "1.0.0"
        }
    }
}