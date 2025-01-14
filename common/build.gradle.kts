import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.codingfeline.buildkonfig)
    alias(libs.plugins.sqldelight)
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
        all {
            languageSettings {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(compose.components.resources)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.lifecycle.viewmodel.compose)
                implementation(libs.sqldelight.runtime)
                api(libs.navigation.compose)

                /* TODO remove it when the Apple targets will be available through JitPack */
                // TODO MQTT LIB ISSUE -> implementation(Libs.mqttLocal)
                // TODO MQTT LIB ISSUE -> implementation(Libs.mqttClientLocal)

                /* TODO use it when the Apple targets will be available through JitPack
                implementation(Libs.mqtt)
                implementation(Libs.mqttClient) */
            }
        }
        val jvm by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(jvm)
            dependencies {
                implementation(libs.activity.compose)
                implementation(libs.core.ktx)
                implementation(libs.firebase.messaging.ktx)
                implementation(libs.ktor.client.android)
                implementation(libs.media3.exoplayer)
                implementation(libs.media3.ui)
                implementation(libs.sqldelight.android.driver)
            }
        }
        val desktopMain by getting {
            dependsOn(jvm)
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.java)
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.ios)
                implementation(libs.sqldelight.native.driver)
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
                implementation(libs.sqldelight.sqljs.driver)
            }
        }
    }
}

android {
    compileSdk = libs.versions.android.compile.sdk.get().toInt()
    namespace = ProjectVersions.PACKAGE_NAME_SHARED
    sourceSets["main"].res.setSrcDirs(listOf("src/commonMain/resources"))
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
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

compose.resources {
    publicResClass = true
    packageOfResClass = "com.niji.claudio.common.resources"
    generateResClass = always
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

// Workaround for first usage
tasks.register("checkGoogleServices") {
    doLast {
        checkGoogleServices()
    }
}

tasks.getByName("preBuild").dependsOn("checkGoogleServices")

fun checkGoogleServices() {
    val googleServices =
        File(project.projectDir.absolutePath + "/../appAndroid/google-services.json")
    if (!googleServices.exists()) {
        val fakeGoogleServices = File(project.projectDir.absolutePath + "/../appAndroid/fake-google-services.json")
        Files.copy(
            fakeGoogleServices.toPath(),
            googleServices.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}
