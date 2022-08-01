pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
plugins {
    id("de.fayard.refreshVersions") version "0.40.2"
}
rootProject.name = "Rainbow"
include(":common", ":desktop", ":android", ":remote", ":domain", ":data", ":local")