package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.api.IHookApi
import com.niji.claudio.common.data.api.Resource
import com.niji.claudio.common.data.feature.hook.IHookRepository
import com.niji.claudio.common.data.model.Slack
import com.niji.claudio.common.tool.LogUtils


class HookRepository(private val api: IHookApi) : AbstractRepository(), IHookRepository {
    override suspend fun sendToken(slack: Slack) {
        val responseResource = sendGeneric { api.sendToken(slack) }
        if (responseResource is Resource.Success) {
            LogUtils.d(TAG, "Success send token to slack :D")
        } else {
            LogUtils.e(TAG, "Error send token to slack :(")
        }
    }

    companion object {
        const val TAG = "HookRepository"
    }
}