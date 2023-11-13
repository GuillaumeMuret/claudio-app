package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class TokenReceivedUseCase(private val token: String) {
    suspend fun execute() {
        RepositoryLocator.userRepository.setUserToken(token)
        RepositoryLocator.userRepository.syncUserDeviceIfPossible(true)
    }
}