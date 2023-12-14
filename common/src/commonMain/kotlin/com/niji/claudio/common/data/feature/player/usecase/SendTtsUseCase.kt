package com.niji.claudio.common.data.feature.player.usecase

import com.niji.claudio.common.data.model.Tts
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.internal.RepositoryLocator

class SendTtsUseCase(private val tts: String, private val user: User) {
    suspend fun execute() =
        RepositoryLocator.playerRepository.sendTts(Tts(message = tts, fromTitle = user.name))
}