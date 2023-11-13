package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class SetUserSleepingMode(private val isSleepingMode: Boolean) {
    suspend fun execute() {
        return RepositoryLocator.userRepository.setSleepingMode(isSleepingMode)
    }
}