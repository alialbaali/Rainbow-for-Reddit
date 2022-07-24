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
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.accompanist:accompanist-navigation-material:0.22.0-rc")
    implementation(AndroidX.Navigation.compose)
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
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        create("release-candidate") {
            versionNameSuffix = "-rc"
            applicationIdSuffix = ".rc"
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}