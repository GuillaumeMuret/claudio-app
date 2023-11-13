package com.niji.claudio.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var bddId: Long? = null,
    var name: String? = null,
    var mPushToken: String? = null,
    var mDeviceServerId: String? = null,
    var selectedServerIdDevice: String? = null,
    var mediaDisplayPreference: String? = null,
    var isSleepingMode: Boolean? = null,
    var isAdmin: Boolean? = null
) {
    companion object {
        const val DIRECTORY_NAME = "claudio"
        const val FILENAME_DEVICES = "devices.json"
        const val FILENAME_USER = "user.json"
        const val FILENAME_MEDIAS = "medias.json"
        const val FILENAME_DOWNLOADED_MEDIAS = "downloaded_medias.json"
        const val FILENAME_FAVORITE = "favorite_medias.json"
        const val FILENAME_DATALOG = "datalog.json"
    }
}