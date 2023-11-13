package com.niji.claudio.common.tool

actual object MqttClientProvider {
    actual suspend fun initAndRunClient(deviceId: String) {
        // TODO
    }

    actual fun publishAtMost(target: String, byteArray: ByteArray) {
        // TODO
    }

    actual fun publishExact(target: String, byteArray: ByteArray) {
        // TODO
    }
}