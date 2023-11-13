package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class SetUserNameUseCase(val name: String) {
    suspend fun execute() {
        RepositoryLocator.userRepository.setUserName(name)
        RepositoryLocator.userRepository.syncUserDeviceIfPossible()
    }
}