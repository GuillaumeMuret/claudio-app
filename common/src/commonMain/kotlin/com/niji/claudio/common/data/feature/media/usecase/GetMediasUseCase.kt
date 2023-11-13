package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class GetMediasUseCase(private val query: String, private val isFavoriteMode: Boolean = false) {
    suspend fun execute(): List<Media> {
        return RepositoryLocator.mediaRepository.getMedias(query, isFavoriteMode)
    }
}
