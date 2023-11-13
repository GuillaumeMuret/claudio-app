package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.internal.RepositoryLocator


class GetUserUseCase {
    suspend fun execute(): User {
        return RepositoryLocator.userRepository.getUser()
    }
}