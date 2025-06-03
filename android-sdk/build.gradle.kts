import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.vanniktech.maven.publish") version "0.31.0"
}

group = "kg.finik"
version = "1.0.6"

android {
    namespace = "kg.finik.android.sdk"
    compileSdk = 35
    ndkVersion = "27.0.12077973"

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
//    Debug dependencies
//    tasks.named("extractDeepLinksDebug").configure {
////        dependsOn("explodekg.finik.flutter_moduleFlutter_releaseDebug")
////        dependsOn("explodekg.finik.flutter_moduleFlutter-debug.aarDebug")
//    }
//    tasks.named("extractDeepLinksRelease") {
//        dependsOn("explodekg.finik.flutter_moduleFlutter_releaseRelease")
//    }
//    tasks.named("bundleDebugLocalLintAar") {
//        dependsOn("mergeJarsDebug")
//    }
//    tasks.named("bundleReleaseLocalLintAar") {
//        dependsOn("mergeJarsRelease")
//    }
//
//    // Release dependencies
//    tasks.matching { it.name == "publishReleasePublicationToMavenCentralRepository" }
//        .configureEach {
//            dependsOn(tasks.matching { it.name == "signReleasePublication" })
//            dependsOn(tasks.matching { it.name == "signMavenPublication" })
//        }
//    tasks.matching { it.name == "publishMavenPublicationToMavenCentralRepository" }.configureEach {
//        dependsOn(tasks.matching { it.name == "signReleasePublication" })
//    }
//
//    // Also ensure signing happens before the staging+release task (used by Vanniktech)
//    tasks.matching { it.name == "publishToMavenCentral" }.configureEach {
//        dependsOn(tasks.matching { it.name == "signReleasePublication" })
//    }
//    tasks.matching { it.name == "publishReleasePublicationToMavenLocal" }.configureEach {
//        dependsOn(tasks.matching { it.name == "signMavenPublication" })
//    }
//    tasks.matching { it.name == "publishMavenPublicationToMavenLocal" }.configureEach {
//        dependsOn(tasks.matching { it.name == "signReleasePublication" })
//    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                artifactId = "android-sdk"

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

dependencies {
    implementation("kg.finik:flutter_release:1.1.1")
//    implementation("kg.finik.flutter_module:flutter_release:1.1.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("script-runtime"))
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
