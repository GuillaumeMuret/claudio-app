package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class SetUserMediaDisplayPreference(val state: String) {
    suspend fun execute() {
        return RepositoryLocator.userRepository.setMediaDisplayPreference(state)
    }
}