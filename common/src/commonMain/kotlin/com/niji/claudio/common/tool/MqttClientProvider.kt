package com.niji.claudio.common.tool

expect object MqttClientProvider {
    suspend fun initAndRunClient(deviceId: String)
    fun publishAtMost(target: String, byteArray: ByteArray)
    fun publishExact(target: String, byteArray: ByteArray)
}
