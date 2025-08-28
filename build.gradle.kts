// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version "8.9.0" apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

buildscript {
    repositories {
        mavenLocal()
        maven("https://jitpack.io") {
            content { includeGroup("com.github.aasitnikov") }
        }
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.github.aasitnikov:fat-aar-android:1.4.2")
    }
}
