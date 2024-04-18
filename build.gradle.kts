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
        // TODO MQTT LIB ISSUE -> Deprecated but useful for mqtt libraries
        // TODO MQTT LIB ISSUE -> jcenter()
        // TODO MQTT LIB ISSUE -> Use maven local for MQTT
        // TODO MQTT LIB ISSUE -> mavenLocal()
    }
}

buildscript {
    val kotlinVersion = extra["kotlin.version"] as String
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(Libs.pluginGoogleServices)
        classpath(Libs.buildkonfigGradlePlugin)
        classpath(Libs.sqlDelightGradlePlugin)
        // To check dependencies updates
        classpath(Libs.benManes)
    }
}
