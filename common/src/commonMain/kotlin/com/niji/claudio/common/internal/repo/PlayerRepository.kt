package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.api.IPlayerApi
import com.niji.claudio.common.data.feature.player.IPlayerRepository
import com.niji.claudio.common.data.feature.user.IUserRepository
import com.niji.claudio.common.data.model.ClaudioData
import com.niji.claudio.common.data.model.FcmBody
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts
import com.niji.claudio.common.data.model.User


class PlayerRepository(
    private val api: IPlayerApi,
    private val userRepository: IUserRepository
) : AbstractRepository(), IPlayerRepository {

    private suspend fun sendGeneric(body: FcmBody) {
        val selectedDevice = userRepository.getSelectedDevice()
        selectedDevice.pushToken?.let { safePushToken ->
            sendGeneric {
                api.send(body.apply {
                    this.to = safePushToken
                })
            }
        }
    }

    override suspend fun sendTts(tts: Tts) {
        sendGeneric(FcmBody.DUMMY_FCM_TTS.apply {
            this.data.tts = tts
        })
    }

    override suspend fun playMedia(media: Media, user: User) {
        sendGeneric(
            FcmBody(
                data = ClaudioData(
                    mediaPlay = Media(
                        serverId = media.serverId,
                        fromTitle = user.name
                    )
                ),
                to = ""
            )
        )
    }

    override suspend fun killPlayer(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_KILL_PLAYER.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }

    override suspend fun volumeMinPlayer(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_VOLUME_MIN.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }

    override suspend fun volumeMaxPlayer(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_VOLUME_MAX.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }

    override suspend fun volumeLowerPlayer(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_VOLUME_LOWER.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }

    override suspend fun volumeRaisePlayer(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_VOLUME_RAISE.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }

    override suspend fun vibrate(user: User) {
        sendGeneric(FcmBody.DUMMY_FCM_VIBRATE.apply {
            this.data.killPlayer?.fromTitle = user.name
        })
    }
}
