package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class SetMustReloadUseCase() {
    fun execute() {
        RepositoryLocator.deviceRepository.setMustReload()
        RepositoryLocator.mediaRepository.setMustReload()
        RepositoryLocator.userRepository.setMustReload()
    }
}