package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class DeleteMediaUseCase(val media: Media) {
    suspend fun execute() {
        return RepositoryLocator.mediaRepository.deleteMedia(media)
    }
}