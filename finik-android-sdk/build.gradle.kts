import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("com.kezong.fat-aar")
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.31.0"
}

group = "io.github.mancho-devs"
version = "1.0.0"

android {
    namespace = "kg.mancho.finik_android_sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        ndk {
            // Filter for architectures supported by Flutter
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

afterEvaluate {
//    tasks.named("extractDeepLinksDebug").configure {
//        dependsOn("explodeSdk.finik.flutter_moduleFlutter_releaseDebug")
////        dependsOn("explodeSdk.finik.flutter_moduleFlutter_debugDebug")
//    }
//    tasks.named("extractDeepLinksRelease") {
//        dependsOn("explodeSdk.finik.flutter_moduleFlutter_releaseRelease")
//    }
//    tasks.named("bundleDebugLocalLintAar") {
//        dependsOn("mergeJarsDebug")
//    }
//    tasks.named("bundleReleaseLocalLintAar") {
//        dependsOn("mergeJarsRelease")
//    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "io.github.mancho-devs"
                artifactId = "finik-android-sdk"
                version = "1.0.0"

                pom {
                    name.set("Finik Android SDK")
                    description.set("Finik SDK for Android")
                    url.set("https://github.com/mancho-devs/finik-android-sdk")
                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("mancho-devs")
                            name.set("Mancho Devs")
                            email.set("kuba@mancho.dev")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/mancho-devs/finik-android-sdk.git")
                        developerConnection.set("scm:git:ssh://github.com/mancho-devs/finik-android-sdk.git")
                        url.set("https://github.com/mancho-devs/finik-android-sdk")
                    }
                }
            }
        }

        repositories {
            mavenLocal()
            maven {
                name = "BuildDir"
                url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

dependencies {
    embed(libs.flutter.release)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

task("assembleFatAar", type = Zip::class) {
    from("build/outputs/aar/")
    into("build/outputs/aar/fat/")
    include("*.aar")
}

