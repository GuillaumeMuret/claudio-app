package com.niji.claudio.common.internal.repo.api

import com.niji.claudio.BuildKonfig
import com.niji.claudio.common.data.api.IHookApi
import com.niji.claudio.common.data.model.Slack
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class SlackApi : AbstractKtorClient(), IHookApi {

    val api = this

    override suspend fun sendToken(slack: Slack) {
        httpClient.post("$BASE_URL/services/${BuildKonfig.SLACK_END_URL}") {
            header("Content-Type", "application/json")
            setBody(slack)
        }
    }

    companion object {
        private const val BASE_URL = "https://hooks.slack.com"
    }
}
