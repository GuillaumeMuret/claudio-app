package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class GetMediaFromServerIdUseCase(private val serverId: String) {
    suspend fun execute(): Media? {
        return RepositoryLocator.mediaRepository.getMediaFromServerId(serverId)
    }
}