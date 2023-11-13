package com.niji.claudio.common.data.feature.player.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.internal.RepositoryLocator


class PlayMediaOnRemoteUseCase(val media: Media, val user: User) {
    suspend fun execute() = RepositoryLocator.playerRepository.playMedia(media, user)
}
