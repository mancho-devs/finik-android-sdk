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
    val storageUrl: String =
        System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"
    repositories {
        google()
        mavenCentral()
//        maven("file:///Users/k.asanov/Projects/averspay-flutter-sdk/flutter_module/build/host/outputs/repo")
        maven("$storageUrl/download.flutter.io")
    }
}

rootProject.name = "FinikAndroidSdk"
include(":app")
include(":android-sdk")
//val filePath = "./flutter_module/.android/include_flutter.groovy"
//apply(from = File(filePath))

