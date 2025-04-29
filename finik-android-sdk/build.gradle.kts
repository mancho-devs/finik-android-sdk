plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("com.kezong.fat-aar")
    id("maven-publish")
    id("signing")
    id("com.github.vlsi.gradle-extensions") version "1.86"
}

// Группа и версия
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    tasks.named("extractDeepLinksDebug") {
        dependsOn("explodeSdk.finik.flutter_moduleFlutter-debug.aarDebug")
    }
    tasks.named("extractDeepLinksRelease") {
        dependsOn("explodeSdk.finik.flutter_moduleFlutter-release.aarRelease")
    }
    tasks.named("bundleReleaseLocalLintAar") {
        dependsOn("mergeJarsRelease")
    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

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

//        repositories {
//            maven {
//                name = "OSSRH"
//                url = uri(
//                    if (version.toString().endsWith("SNAPSHOT"))
//                        "https://s01.oss.sonatype.org/content/repositories/snapshots"
//                    else
//                        "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
//                )
//                credentials {
//                    username = project.findProperty("ossrhUsername") as String?
//                    password = project.findProperty("ossrhPassword") as String?
//                }
//            }
//        }
    }

//    signing {
//        useGpgCmd()
//        sign(publishing.publications["release"])
//    }
}

fataar {
    transitive = true
}

dependencies {
    embed(project(":flutter"))
//    implementation(project(":flutter"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    debugImplementation("sdk.finik.flutter_module:flutter_debug:1.0")
//    releaseImplementation("sdk.finik.flutter_module:flutter_release:1.0")
}

task("assembleFatAar", type = Zip::class) {
    from("build/outputs/aar/")
    into("build/outputs/aar/fat/")
    include("*.aar")
}