package com.niji.claudio.common.tool

// TODO MQTT LIB ISSUE -> import MQTTClient
// TODO MQTT LIB ISSUE -> import TLSClientSettings
import com.niji.claudio.common.data.model.ClaudioData
import com.niji.claudio.common.data.model.User

// TODO MQTT LIB ISSUE -> import mqtt.Subscription
// TODO MQTT LIB ISSUE -> import mqtt.packets.Qos
// TODO MQTT LIB ISSUE -> import mqtt.packets.mqttv5.SubscriptionOptions

@OptIn(ExperimentalUnsignedTypes::class)
actual object MqttClientProvider {

    private const val BASE_TOPIC_VOICE = "claudio/voice"
    private const val BASE_TOPIC_COMMAND = "claudio/command"
    private const val TAG = "MqttService"
    // TODO MQTT LIB ISSUE ->private var client: MQTTClient? = null

    actual suspend fun initAndRunClient(deviceId: String) {
        /* TODO MQTT LIB ISSUE :
        try {
            if (client == null) {
                val destinationPath = FileUtils.generateTls()
                client = MQTTClient(
                    mqttVersion = 5,
                    address = BuildKonfig.MQTT_ADDRESS,
                    port = BuildKonfig.MQTT_PORT,
                    tls = TLSClientSettings(
                        serverCertificatePath = destinationPath,
                    ),
                    userName = BuildKonfig.MQTT_USERNAME,
                    password = BuildKonfig.MQTT_PASSWORD.encodeToByteArray()
                        .toUByteArray(),
                ) { mqttMessage ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = GetUserUseCase().execute()
                        if (mqttMessage.topicName.startsWith(BASE_TOPIC_VOICE)) {
                            if (user.isSleepingMode == false) {
                                mqttMessage.payload?.toByteArray()
                                    ?.let { VoicePlayerService.play(it) }
                            }
                        } else if (mqttMessage.topicName.startsWith(BASE_TOPIC_COMMAND)) {
                            CoroutineScope(CoroutineDispatcherProvider.io()).launch {
                                mqttMessage.payload?.toByteArray()?.decodeToString()?.let {
                                    LogUtils.d(TAG, "Message -> $it")
                                    val claudioData = Json.decodeFromString<ClaudioData>(it)
                                    parseData(claudioData, user)
                                }
                            }
                        }
                    }
                }
                client?.subscribe(
                    listOf(
                        Subscription(
                            "${BASE_TOPIC_VOICE}/${deviceId}",
                            SubscriptionOptions(Qos.AT_MOST_ONCE)
                        ),
                        Subscription(
                            "${BASE_TOPIC_COMMAND}/${deviceId}",
                            SubscriptionOptions(Qos.EXACTLY_ONCE)
                        ),
                    )
                )
                LogUtils.d(TAG, "TOPIC VOICE = ${BASE_TOPIC_VOICE}/${deviceId}")
                client?.run() // Blocking method, use step() if you don't want to block the thread
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "Issues ?", e)
        } finally {
            client = null
        }
         */
    }

    private suspend fun parseData(claudioData: ClaudioData, user: User) {
        if (user.isSleepingMode == true) {
            MqttUtils.createNotification(claudioData)
        } else {
            claudioData.tts?.let { PlayerUtils.playTts(it) }
            claudioData.mediaPlay?.let { PlayerUtils.displayPlayerIfPossible(it) }
            claudioData.volumeRaise?.let { DeviceUtils.raiseVolume() }
            claudioData.volumeLower?.let { DeviceUtils.lowerVolume() }
            claudioData.volumeMax?.let { DeviceUtils.maxVolume() }
            claudioData.volumeMin?.let { DeviceUtils.minVolume() }
            claudioData.killPlayer?.let { PlayerUtils.killPlayer() }
            claudioData.vibrate?.let { DeviceUtils.vibrate() }
        }
        MqttUtils.logMessage(claudioData, user.isSleepingMode == true)
    }

    actual fun publishAtMost(target: String, byteArray: ByteArray) {
        /* TODO MQTT LIB ISSUE :
        val mqttTarget = "${BASE_TOPIC_VOICE}/$target"
        try {
            client?.publish(
                false,
                Qos.AT_MOST_ONCE,
                mqttTarget,
                byteArray.toUByteArray()
            )
        } catch (e: Exception) {
            LogUtils.e(TAG, "Publish at most issues :(", e)
        }
         */
    }

    actual fun publishExact(target: String, byteArray: ByteArray) {
        /* TODO MQTT LIB ISSUE :
        val mqttTarget = "${BASE_TOPIC_COMMAND}/${target}"
        try {
            client?.publish(
                false,
                Qos.EXACTLY_ONCE,
                mqttTarget,
                byteArray.toUByteArray()
            )
        } catch (e: Exception) {
            LogUtils.e(TAG, "Publish exact issues :(", e)
        }
         */
    }
}