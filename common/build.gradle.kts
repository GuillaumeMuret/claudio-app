import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
    id("com.github.ben-manes.versions")
    id(Libs.sqlDelightPlugin)
}

val properties = getMyProperties()

kotlin {
    androidTarget()
    jvm("desktop")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }
    js(IR) {
        browser()
        nodejs()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(Libs.ktorClientCore)
                implementation(Libs.ktorClientContentNegotiation)
                implementation(Libs.ktorSerializationKotlinxJson)
                implementation(Libs.gson)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                /* TODO remove it when the Apple targets will be available through JitPack */
                // TODO MQTT LIB ISSUE -> implementation(Libs.mqttLocal)
                // TODO MQTT LIB ISSUE -> implementation(Libs.mqttClientLocal)

                /* TODO use it when the Apple targets will be available through JitPack
                implementation(Libs.mqtt)
                implementation(Libs.mqttClient) */

                implementation(Libs.sqlDelightRuntime)
            }
        }
        val jvm by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(jvm)
            dependencies {
                implementation(Libs.appcompat)
                implementation(Libs.activityCompose)
                implementation(Libs.coreKtx)
                implementation(Libs.firebaseMessaging)
                implementation(Libs.ktorClientAndroid)
                implementation(Libs.media3ExoPlayer)
                implementation(Libs.media3Ui)
                implementation(Libs.sqlDelightAndroidDriver)
            }
        }
        val desktopMain by getting {
            dependsOn(jvm)
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(Libs.ktorClientJava)
                implementation(Libs.sqlDelightSqliteDriver)
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(Libs.ktorClientIos)
                implementation(Libs.gson)
                implementation(Libs.sqlDelightNativeDriver)
            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(Libs.sqlDelightJsDriver)
            }
        }
    }
}

android {
    compileSdk = ProjectVersions.COMPILE_SDK
    namespace = ProjectVersions.PACKAGE_NAME_SHARED
    sourceSets["main"].res.setSrcDirs(listOf("src/commonMain/resources"))
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = ProjectVersions.MIN_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

// https://github.com/ben-manes/gradle-versions-plugin
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

buildkonfig {
    packageName = ProjectVersions.PACKAGE_NAME
    defaultConfigs {
        buildConfigField(
            INT,
            "MQTT_PORT",
            properties.getProperty("MQTT_PORT") ?: "1883"
        )
        buildConfigField(
            STRING,
            "MQTT_ADDRESS",
            properties.getProperty("MQTT_ADDRESS") ?: "192.168.1.1"
        )
        buildConfigField(
            STRING,
            "MQTT_USERNAME",
            properties.getProperty("MQTT_USERNAME") ?: "MQTT_USERNAME"
        )
        buildConfigField(
            STRING,
            "MQTT_PASSWORD",
            properties.getProperty("MQTT_PASSWORD") ?: "MQTT_PASSWORD"
        )
        buildConfigField(
            STRING,
            "SLACK_END_URL",
            properties.getProperty("SLACK_END_URL") ?: "SLACK_END_URL"
        )
        buildConfigField(
            STRING,
            "FCM_SERVER_KEY",
            properties.getProperty("FCM_SERVER_KEY") ?: "FCM_SERVER_KEY"
        )
        buildConfigField(
            STRING,
            "CLAUDIO_BASE_URL",
            properties.getProperty("CLAUDIO_BASE_URL") ?: "https://my-claudio-base-url.io"
        )
        buildConfigField(
            STRING,
            "CLAUDIO_AUTH_TOKEN",
            properties.getProperty("CLAUDIO_AUTH_TOKEN") ?: "CLAUDIO_AUTH_TOKEN"
        )
        buildConfigField(
            BOOLEAN,
            "IS_USING_FCM",
            properties.getProperty("IS_USING_FCM") ?: "true"
        )
    }
}

sqldelight {
    database("ClaudioDatabaseDelight") {
        packageName = "com.niji.claudio.common.internal.repo.save"
    }
}

fun getMyProperties(propertyFileName: String = "local"): Properties {
    val properties = Properties()
    val propertiesFile = rootProject.file("${propertyFileName}.properties")
    if (propertiesFile.isFile) {
        InputStreamReader(FileInputStream(propertiesFile), Charsets.UTF_8)
            .use { reader ->
                properties.load(reader)
            }
    } else error("File from not found")
    // Used for MQTT certificate
    val mqttTlsCertificate =
        File(project.projectDir.absolutePath + "/src/commonMain/resources/raw/tls.crt")
    if (!mqttTlsCertificate.exists()) {
        mqttTlsCertificate.createNewFile()
        mqttTlsCertificate.appendText(
            properties.getProperty("MQTT_TLS_CERTIFICATE") ?: "MQTT_TLS_CERTIFICATE"
        )
    }
    return properties
}
