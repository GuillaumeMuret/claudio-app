plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(ProjectVersions.MAVEN_JETBRAINS_COMPOSE)
        maven(ProjectVersions.MAVEN_JITPACK)
        // Deprecated but useful for mqtt libraries
        jcenter()
        // Use maven local for MQTT
        mavenLocal()
    }
}

buildscript {
    val kotlinVersion = extra["kotlin.version"] as String
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(Libs.pluginGoogleServices)
        classpath(Libs.buildkonfigGradlePlugin)
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelightVersion}")
        // To check dependencies updates
        classpath("com.github.ben-manes:gradle-versions-plugin:0.38.0")
    }
}
