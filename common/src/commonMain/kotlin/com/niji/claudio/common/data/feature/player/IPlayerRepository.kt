package com.niji.claudio.common.data.feature.player

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts
import com.niji.claudio.common.data.model.User


interface IPlayerRepository {
    suspend fun sendTts(tts: Tts)
    suspend fun playMedia(media: Media, user: User)
    suspend fun killPlayer(user: User)
    suspend fun volumeMinPlayer(user: User)
    suspend fun volumeMaxPlayer(user: User)
    suspend fun volumeLowerPlayer(user: User)
    suspend fun volumeRaisePlayer(user: User)
    suspend fun vibrate(user: User)
}
