package com.niji.claudio.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Device(
    var bddId: Long? = null,
    var name: String? = null,
    @SerialName("push_token")
    var pushToken: String? = null,
    @SerialName("id")
    var serverId: String? = null,
) {

    fun postDevicesBody(): Device {
        return Device(name = name, pushToken = pushToken)
    }

    companion object {
        const val NO_SERVER_ID = "no_server_id"
        const val NO_PUSH_TOKEN = "no_push_token"
        fun noTokenCurrentDevice(name: String): Device {
            return Device(
                name = name,
                pushToken = NO_PUSH_TOKEN,
                serverId = NO_SERVER_ID
            )
        }
    }
}
