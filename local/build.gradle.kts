plugins {
    kotlin("jvm") version "1.4.21"
}

version = "unspecified"
val exposedVersion = "0.28.1"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}
