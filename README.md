# Nightowl Custom SystemUI

SystemUI built entirely with Jetpack Compose and a Clean MVVM architecture at least a Start :P.

This project completely decouples the monolithic AOSP `CarSystemUI` legacy XML structure.

## AOSP Integration Guide

Because this SystemUI is custom-built, it utilizes the package name `com.hpnightowl.systemui`. To successfully integrate this into a custom AOSP ROM, you must configure the Android build system to include it and boot it.

### 1. The `Android.bp` Configuration
To compile this app inside an AOSP tree (e.g., in `packages/apps/NightowlSystemUI`), you must define an `Android.bp` file. Since we rely on Jetpack Compose and hidden APIs, your blueprint should look like this:

```bp
android_app {
    name: "NightowlSystemUI",
    
    srcs: [
        "app/src/main/java/**/*.kt",
        "app/src/main/java/**/*.java",
    ],
    
    resource_dirs: ["app/src/main/res"],
    
    manifest: "app/src/main/AndroidManifest.xml",
    
    platform_apis: true,
    certificate: "platform",
    privileged: true,
    
    static_libs: [
        "androidx.compose.ui_ui",
        "androidx.compose.material_material",
        "androidx.compose.ui_ui-tooling-preview",
        "androidx.lifecycle_lifecycle-runtime-ktx",
        "androidx.activity_activity-compose",
    ],
    
    optimize: {
        enabled: false,
    },
}
```

### 2. Booting the Custom Package (Overriding `config.xml`)
The Android OS framework is hardcoded to boot the default `com.android.systemui`. You must change this pointer in your AOSP source tree so it boots our custom `com.hpnightowl.systemui` instead.

Navigate to `frameworks/base/core/res/res/values/config.xml` in your AOSP tree and modify the `config_systemUIServiceComponent`:

```xml
<!-- REPLACE THIS: -->
<!-- <string name="config_systemUIServiceComponent" translatable="false">com.android.systemui/com.android.systemui.SystemUIService</string> -->

<!-- WITH THIS: -->
<string name="config_systemUIServiceComponent" translatable="false">com.hpnightowl.systemui/com.hpnightowl.systemui.service.SystemUIService</string>
```

### 3. Replacing the Default Package
Ensure that `NightowlSystemUI` is added to your device's `PRODUCT_PACKAGES` list (e.g., in `device/your_oem/your_device/device.mk`), and ensure that the legacy `CarSystemUI` is removed to prevent conflicts.

```makefile
# In device.mk
PRODUCT_PACKAGES += NightowlSystemUI
# PRODUCT_PACKAGES -= CarSystemUI (Ensure old one is removed) or you can override it in Androi.bp using overrides 
```

## Local Development (Android Studio)
For rapid local iteration, this project can be compiled directly via Gradle in Android Studio.
1. Run `./deploy.sh` to compile, push via ADB, and instantly hot-restart the SystemUI process on your emulator!
