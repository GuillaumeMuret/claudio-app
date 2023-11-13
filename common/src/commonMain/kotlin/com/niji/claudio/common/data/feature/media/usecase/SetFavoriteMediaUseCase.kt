package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class SetFavoriteMediaUseCase(private val media: Media) {
    suspend fun execute() = RepositoryLocator.mediaRepository.setFavoriteMediaUseCase(media)
}