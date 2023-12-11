package com.niji.claudio.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Device(
    @SerialName("id")
    var serverId: String? = null,
    var name: String? = null,
    @SerialName("push_token")
    var pushToken: String? = null,
) {
    companion object {
        const val NO_SERVER_ID = "no_server_id"
        const val NO_PUSH_TOKEN = "no_push_token"
        fun noTokenCurrentDevice(name: String): Device {
            return Device(
                serverId = NO_SERVER_ID,
                name = name,
                pushToken = NO_PUSH_TOKEN,
            )
        }
    }
}
