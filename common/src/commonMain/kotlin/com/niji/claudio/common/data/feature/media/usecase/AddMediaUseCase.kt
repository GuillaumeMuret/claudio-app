package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class AddMediaUseCase(val media: Media) {
    suspend fun execute(): Media? {
        return RepositoryLocator.mediaRepository.addMedia(media)
    }
}