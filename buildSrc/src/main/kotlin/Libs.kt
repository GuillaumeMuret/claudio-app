object Libs {
    // Common
    const val appcompat: String = "androidx.appcompat:appcompat:" + Versions.appcompat
    const val coreKtx: String = "androidx.core:core-ktx:" + Versions.coreKtx
    const val ktorClientCore : String = "io.ktor:ktor-client-core:" + Versions.ktor
    const val ktorClientContentNegotiation : String = "io.ktor:ktor-client-content-negotiation:" + Versions.ktor
    const val ktorSerializationKotlinxJson : String = "io.ktor:ktor-serialization-kotlinx-json:" + Versions.ktor
    const val ktorClientIos : String = "io.ktor:ktor-client-ios:" + Versions.ktor
    const val ktorClientAndroid : String = "io.ktor:ktor-client-android:" + Versions.ktor
    const val ktorClientJava : String = "io.ktor:ktor-client-java:" + Versions.ktor
    const val ktorClientJs : String = "io.ktor:ktor-client-js:" + Versions.ktor
    const val gson : String = "com.google.code.gson:gson:" + Versions.gson
    const val buildkonfigGradlePlugin : String = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:" + Versions.buildkonfigGradlePlugin

    // TODO remove it when the Apple targets will be available through JitPack
    const val mqttLocal: String = "com.github.davidepianca98:kmqtt-common:" + Versions.mqtt
    const val mqttClientLocal: String = "com.github.davidepianca98:kmqtt-client:" + Versions.mqtt

    // TODO use it when the Apple targets will be available through JitPack
    const val mqtt: String = "com.github.davidepianca98.KMQTT:kmqtt-common:" + Versions.mqtt
    const val mqttClient: String = "com.github.davidepianca98.KMQTT:kmqtt-client:" + Versions.mqtt

    // Android specific
    const val activityCompose : String = "androidx.activity:activity-compose:" + Versions.activityCompose
    const val firebaseMessaging : String = "com.google.firebase:firebase-messaging-ktx:" + Versions.firebaseMessaging
    const val pluginGoogleServices : String = "com.google.gms:google-services:" + Versions.googleServices
    const val media3ExoPlayer: String = "androidx.media3:media3-exoplayer:" + Versions.media3
    const val media3Ui: String = "androidx.media3:media3-ui:" + Versions.media3
}
