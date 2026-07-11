#!/bin/bash
cd /home/ubuntu/AndroidStudioProjects/systemui
set -e
echo "Building APK..."
./gradlew assembleDebug
echo "Pushing APK..."
adb root
adb remount
adb push app/build/outputs/apk/debug/app-debug.apk /system_ext/priv-app/CarSystemUI/CarSystemUI.apk
echo "Restarting SystemUI..."
adb shell killall com.android.systemui || true
echo "Done!"
