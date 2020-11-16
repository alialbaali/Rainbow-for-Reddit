import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.compose") version "0.1.0-build113"
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
    implementation(compose.desktop.all)
    implementation(compose.materialIconsExtended)
    implementation(KotlinX.Coroutines.core)
    implementation(project(":data"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}


application {
    mainClassName = "MainKt"
}