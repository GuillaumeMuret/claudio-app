package com.niji.claudio.common.data.feature.hook.usecase

import com.niji.claudio.common.data.model.Slack
import com.niji.claudio.common.internal.RepositoryLocator


class HookTokenUseCase(private val slack: Slack) {
    suspend fun execute() = RepositoryLocator.hookRepository.sendToken(slack)
}