plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(project(":common"))
}

android {
    compileSdk = libs.versions.android.compile.sdk.get().toInt()
    namespace = ProjectVersions.PACKAGE_NAME_ANDROID
    defaultConfig {
        applicationId = ProjectVersions.PACKAGE_NAME_ANDROID
        minSdk = libs.versions.android.min.sdk.get().toInt()
        targetSdk = libs.versions.android.target.sdk.get().toInt()
        versionCode = ProjectVersions.getAppVersionCode()
        versionName = ProjectVersions.getAppVersionName()
        setProperty("archivesBaseName", "${ProjectVersions.APP_NAME}-$versionName-$versionCode")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
