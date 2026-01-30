########################################
# Flutter embedding (REQUIRED)
########################################

-keep class io.flutter.** { *; }
-keep class io.flutter.plugins.** { *; }
-keep class io.flutter.util.** { *; }
-keep class io.flutter.view.** { *; }
-keep class io.flutter.embedding.** { *; }

-dontwarn io.flutter.embedding.**
