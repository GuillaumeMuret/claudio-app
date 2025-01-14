object Libs {
    private const val MQTT = "0.4.1"

    // TODO remove it when the Apple targets will be available through JitPack
    const val mqttLocal: String = "com.github.davidepianca98:kmqtt-common:$MQTT"
    const val mqttClientLocal: String = "com.github.davidepianca98:kmqtt-client:$MQTT"

    // TODO use it when the Apple targets will be available through JitPack
    const val mqtt: String = "com.github.davidepianca98.KMQTT:kmqtt-common:$MQTT"
    const val mqttClient: String = "com.github.davidepianca98.KMQTT:kmqtt-client:$MQTT"
}
