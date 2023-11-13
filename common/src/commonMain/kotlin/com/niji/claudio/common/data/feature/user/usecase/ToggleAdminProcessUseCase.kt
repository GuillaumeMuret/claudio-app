package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class ToggleAdminProcessUseCase(private val btn: String) {
    suspend fun execute() = RepositoryLocator.userRepository.setBtnClick(btn)
}