import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.4.20"
    id("org.jetbrains.compose") version "0.3.0-build135"
    application
}

group = "me.ali"
version = "0.1"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(project(":data"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
}

kotlin {
    target.compilations.all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xallow-result-return-type"
        }
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = freeCompilerArgs + "-Xallow-result-return-type"
        }
    }
}

application {
    mainClass.set("com.rainbow.app.MainKt")
}