import java.net.URI

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.codingfeline.buildkonfig) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.google.services) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(URI.create("https://maven.pkg.jetbrains.space/public/p/compose/dev"))
        maven(URI.create("https://jitpack.io"))
        // TODO MQTT LIB ISSUE -> Deprecated but useful for mqtt libraries
        // TODO MQTT LIB ISSUE -> jcenter()
        // TODO MQTT LIB ISSUE -> Use maven local for MQTT
        // TODO MQTT LIB ISSUE -> mavenLocal()
    }
}
