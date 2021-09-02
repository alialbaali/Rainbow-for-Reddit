
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

}

rootProject.name = "Rainbow"

include("app", "remote", "domain", "data", "local")

plugins {
    id("de.fayard.refreshVersions") version "0.20.0"
}
