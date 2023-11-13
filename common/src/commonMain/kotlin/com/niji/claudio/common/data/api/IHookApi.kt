package com.niji.claudio.common.data.api

import com.niji.claudio.common.data.model.Slack


interface IHookApi {
    suspend fun sendToken(slack: Slack)
}