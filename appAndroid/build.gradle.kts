plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
}

dependencies {
    implementation(project(":common"))
}

android {
    compileSdk = ProjectVersions.COMPILE_SDK
    namespace = ProjectVersions.PACKAGE_NAME_ANDROID
    defaultConfig {
        applicationId = ProjectVersions.PACKAGE_NAME_ANDROID
        minSdk = ProjectVersions.MIN_SDK
        targetSdk = ProjectVersions.TARGET_SDK
        versionCode = ProjectVersions.getAppVersionCode()
        versionName = ProjectVersions.getAppVersionName()
        setProperty("archivesBaseName", "${ProjectVersions.APP_NAME}-$versionName-$versionCode")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    signingConfigs {
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "cl@udioDebugAlias"
            storeFile = file("./keystore/debug-keystore.jks")
            storePassword = "cl@udioDebug"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
}
