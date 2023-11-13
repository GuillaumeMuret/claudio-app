package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class GetUserMediaDisplayPreference {
    suspend fun execute(): String {
        return RepositoryLocator.userRepository.getMediaDisplayPreference()
    }
}