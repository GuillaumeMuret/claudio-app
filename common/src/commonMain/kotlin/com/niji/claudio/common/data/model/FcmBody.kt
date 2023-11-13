package com.niji.claudio.common.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FcmBody(
    var to: String,
    val data: ClaudioData,
) {
    companion object {
        val DUMMY_FCM_MEDIA_PLAY = FcmBody(
            data = ClaudioData(
                mediaPlay = Media(
                    serverId = "serverId1"
                )
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_KILL_PLAYER = FcmBody(
            data = ClaudioData(
                killPlayer = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_VOLUME_MAX = FcmBody(
            data = ClaudioData(
                volumeMax = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_VOLUME_MIN = FcmBody(
            data = ClaudioData(
                volumeMin = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_VOLUME_RAISE = FcmBody(
            data = ClaudioData(
                volumeRaise = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_VOLUME_LOWER = FcmBody(
            data = ClaudioData(
                volumeLower = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_VIBRATE = FcmBody(
            data = ClaudioData(
                vibrate = ClaudioSubData()
            ),
            to = "TODDO"
        )
        val DUMMY_FCM_TTS = FcmBody(
            data = ClaudioData(
                tts = Tts(
                    message = "Salut Niji ! Bien ou bien ?",
                    language = "fr",
                    pitch = 1F,
                    speed = 1.5F
                )
            ),
            to = "TODDO"
        )
    }
}

@Serializable
data class ClaudioData(
    @SerialName("kill_player")
    val killPlayer: ClaudioSubData? = null,
    @SerialName("volume_max")
    val volumeMax: ClaudioSubData? = null,
    @SerialName("volume_min")
    val volumeMin: ClaudioSubData? = null,
    @SerialName("volume_raise")
    val volumeRaise: ClaudioSubData? = null,
    @SerialName("volume_lower")
    val volumeLower: ClaudioSubData? = null,
    @SerialName("vibrate")
    val vibrate: ClaudioSubData? = null,
    @Contextual
    @SerialName("media_play")
    val mediaPlay: Media? = null,
    @SerialName("tts")
    var tts: Tts? = null
)

@Serializable
data class ClaudioSubData(
    @SerialName("fromTitle")
    var fromTitle: String? = null,
)
