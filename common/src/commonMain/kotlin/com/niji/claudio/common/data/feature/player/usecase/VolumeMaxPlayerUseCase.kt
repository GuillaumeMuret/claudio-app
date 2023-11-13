package com.niji.claudio.common.data.feature.player.usecase

import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.internal.RepositoryLocator


class VolumeMaxPlayerUseCase(private val user: User) {
    suspend fun execute() = RepositoryLocator.playerRepository.volumeMaxPlayer(user)
}