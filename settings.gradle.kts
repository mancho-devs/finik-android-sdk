pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")
        maven(url = "./flutter_module/build/host/outputs/repo")
    }
}

rootProject.name = "FinikAndroidSdk"
include(":app")
include(":finik-android-sdk")
//include(":flutter")
//project(":flutter").projectDir = file("./flutter_module")
//val filePath = "./flutter_module/.android/include_flutter.groovy"
//apply(from = File(filePath))

