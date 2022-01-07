plugins {
    id("org.jetbrains.compose") version "1.0.1"
    id("com.android.application")
    kotlin("android")
}

group = "com.alialbaali"
version = "1.0"

repositories {
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.alialbaali.android"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}