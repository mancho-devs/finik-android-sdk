pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
//        maven("file://$rootDir/flutter_module/build/host/outputs/repo") // your local module AARs
        maven("https://storage.googleapis.com/download.flutter.io")     // Flutter engine AARs
    }
}

rootProject.name = "FinikAndroidSdk"
include(":app")
include(":finik-android-sdk")
//val filePath = "./flutter_module/.android/include_flutter.groovy"
//apply(from = File(filePath))

