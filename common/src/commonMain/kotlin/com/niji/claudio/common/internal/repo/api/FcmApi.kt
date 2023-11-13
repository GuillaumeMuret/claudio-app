package com.niji.claudio.common.internal.repo.api

import com.niji.claudio.BuildKonfig
import com.niji.claudio.common.data.api.IPlayerApi
import com.niji.claudio.common.data.model.FcmBody
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class FcmApi : AbstractKtorClient(), IPlayerApi {

    val api = this

    override suspend fun send(fcmBody: FcmBody) {
        httpClient.post("$BASE_URL/send") {
            header("Content-Type", "application/json")
            header("Authorization", "key=${BuildKonfig.FCM_SERVER_KEY}")
            setBody(fcmBody)
        }
    }

    companion object {
        private const val BASE_URL = "https://fcm.googleapis.com/fcm"
    }
}
