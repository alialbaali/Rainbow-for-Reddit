import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.4.21-2"))
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xallow-result-return-type")
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }

    group = "com.rainbow"
    version = "0.0.5"
}