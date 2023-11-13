package com.niji.claudio.common.data.api

import com.niji.claudio.common.data.model.FcmBody


interface IPlayerApi {
    suspend fun send(fcmBody: FcmBody)
}