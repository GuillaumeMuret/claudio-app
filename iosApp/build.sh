#!/bin/bash

set -e

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
cd ${SCRIPT_DIR}

# https://ajpagente.github.io/mobile/xcb-with-xcconfig/
# xcodebuild -project <your-project-name>.xcodeproj -scheme <scheme name> -archivePath <path/to/your/app>.xcarchive archive
xcodebuild -project iosApp.xcodeproj -scheme "iosApp" -archivePath iosApp.xcarchive archive

# xcodebuild -exportArchive -archivePath <path/to/your/app>.xcarchive -exportPath <path/to/ipa/output/folder> -exportOptionsPlist <path/to/ExportOptions>.plist
xcodebuild -exportArchive -archivePath iosApp.xcarchive -exportPath out -exportOptionsPlist export.plist
