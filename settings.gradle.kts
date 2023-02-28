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
    id("de.fayard.refreshVersions") version "0.51.0"
}
rootProject.name = "Rainbow"
include(":desktop", ":remote", ":domain", ":data", ":local")