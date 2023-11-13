package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class GetLocalMediasUseCase(private val query: String, private val isFavoriteMode: Boolean) {
    suspend fun execute(): List<Media> {
        return RepositoryLocator.mediaRepository.getLocalMedias(query, isFavoriteMode)
    }
}