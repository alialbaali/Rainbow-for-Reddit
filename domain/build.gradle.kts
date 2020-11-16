plugins {
    kotlin("jvm") version "1.4.10"
}

version = "unspecified"

repositories {
    jcenter()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven(url = "https://kotlin.bintray.com/kotlinx/")
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
}
