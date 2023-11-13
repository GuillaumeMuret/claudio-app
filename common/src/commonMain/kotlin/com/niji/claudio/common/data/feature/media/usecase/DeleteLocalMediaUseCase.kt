package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.internal.RepositoryLocator


class DeleteLocalMediaUseCase(private val filePath: String) {
    suspend fun execute() {
        RepositoryLocator.mediaRepository.deleteLocalMedia(
            RepositoryLocator.mediaRepository.getMediaFromFilePath(filePath)
                ?: Media(filePath = filePath)
        )
    }
}