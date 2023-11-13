package com.niji.claudio.common.data.feature.hook

import com.niji.claudio.common.data.model.Slack


interface IHookRepository {
    suspend fun sendToken(slack: Slack)
}