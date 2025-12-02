# Tuya Demo Android

This repository contains a minimal Android demo app built with the Tuya (Thingclips) Basic SDK 6.11.0. The sample is implemented in Kotlin with view binding enabled and demonstrates:

- Email/phone login
- EZ mode Wi-Fi pairing
- BLE scanning
- Device list retrieval
- Simple DP control (on/off)

BizBundle components are intentionally excluded so the project relies solely on the Basic SDK packages. The project targets Android 34 and uses minSdk 23.

## Building

1. Open the project in Android Studio.
2. Ensure the `compileSdk`/`targetSdk` are available (API 34) and that the Android Gradle Plugin can download dependencies from the Tuya Maven repositories.
3. Sync and build; the included Gradle wrapper targets Gradle 8.7 and AGP 8.5.2.

## Notes

- Enter your own Tuya/Thingclips AppKey and AppSecret on the login screen before authenticating.
- Replace the placeholder account credentials on the login screen with valid test credentials for your cloud project.
