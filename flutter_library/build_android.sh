#!/usr/bin/env sh

flutter pub get

# Read version number from command-line argument
version="$1"
mode="$2"

if [ -z "$version" ] || [ -z "$mode" ]; then
    echo "Usage: $0 <version> <mode>"
    echo "Mode should be 'debug' or 'release'."
    exit 1
fi

# Validate build mode
if [ "$mode" != "debug" ] && [ "$mode" != "release" ]; then
    echo "Invalid mode. Mode should be 'debug' or 'release'."
    exit 1
fi

# Read the configs file to get maven-url, maven-user, maven-password
read_property() {
    local prop_file="$1"
    local key="$2"
    local value=$(grep -E "^$key=" "$prop_file" | cut -d'=' -f2)
    echo "$value"
}

# Find flutter-root
find_flutter_root() {
    # Use 'which' to find the location of the 'flutter' command
    local flutter_path=$(which flutter)
    # If 'flutter' is found, extract the parent directory as the Flutter root
    if [ -x "$flutter_path" ]; then
        local flutter_root=$(dirname $(dirname "$flutter_path"))
        echo "$flutter_root"
    else
        echo "Flutter not found. Please ensure Flutter is installed and accessible in your PATH."
        exit 1
    fi
}

# Path to the properties file
properties_file="deploy-configs.properties"
# Read properties from file
maven_url=$(read_property "$properties_file" "repositoryUrl")
maven_user=$(read_property "$properties_file" "ossrhUsername")
maven_pwd=$(read_property "$properties_file" "ossrhPassword")

# Find Flutter root directory
flutter_root=$(find_flutter_root)

cd .android

# Determine the Gradle task based on build mode
if [ "$mode" = "debug" ]; then
    assemble_task="assembleAarDebug"
elif [ "$mode" = "release" ]; then
    assemble_task="assembleAarRelease"
fi

./gradlew \
  -I=../aar_init_script.gradle \
  -Pmaven-url="$maven_url" \
  -Pmaven-user="$maven_user" \
  -Pmaven-pwd="$maven_pwd" \
  -Pis-plugin=false \
  -PbuildNumber=${pom-${version}} \
  -Pflutter-root="$flutter_root" \
    -Ptarget-platform=android-arm,android-arm64,android-x64 "$assemble_task" \