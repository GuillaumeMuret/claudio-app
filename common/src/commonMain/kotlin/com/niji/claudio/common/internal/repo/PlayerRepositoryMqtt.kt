package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.feature.player.IPlayerRepository
import com.niji.claudio.common.data.feature.user.IUserRepository
import com.niji.claudio.common.data.model.ClaudioData
import com.niji.claudio.common.data.model.ClaudioSubData
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.tool.MqttClientProvider
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class PlayerRepositoryMqtt(
    private val userRepository: IUserRepository
) : AbstractRepository(), IPlayerRepository {

    private suspend fun sendGeneric(data: ClaudioData) {
        val selectedDevice = userRepository.getSelectedDevice()
        selectedDevice.serverId?.let {
            MqttClientProvider.publishExact(
                it,
                Json.encodeToString(data).toByteArray()
            )
        }
    }

    override suspend fun sendTts(tts: Tts) {
        sendGeneric(ClaudioData(tts = tts))
    }

    override suspend fun playMedia(media: Media, user: User) {
        sendGeneric(
            ClaudioData(
                mediaPlay = Media(
                    serverId = media.serverId,
                    fromTitle = user.name
                )
            )
        )
    }

    override suspend fun killPlayer(user: User) {
        sendGeneric(ClaudioData(killPlayer = ClaudioSubData(fromTitle = user.name)))
    }

    override suspend fun volumeMinPlayer(user: User) {
        sendGeneric(ClaudioData(volumeMin = ClaudioSubData(fromTitle = user.name)))
    }

    override suspend fun volumeMaxPlayer(user: User) {
        sendGeneric(ClaudioData(volumeMax = ClaudioSubData(fromTitle = user.name)))
    }

    override suspend fun volumeLowerPlayer(user: User) {
        sendGeneric(ClaudioData(volumeLower = ClaudioSubData(fromTitle = user.name)))
    }

    override suspend fun volumeRaisePlayer(user: User) {
        sendGeneric(ClaudioData(volumeRaise = ClaudioSubData(fromTitle = user.name)))
    }

    override suspend fun vibrate(user: User) {
        sendGeneric(ClaudioData(vibrate = ClaudioSubData(fromTitle = user.name)))
    }
}
