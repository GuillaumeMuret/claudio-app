package com.niji.claudio.common.internal.repo.api

import com.niji.claudio.common.tool.NetworkUtils
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

abstract class AbstractKtorClient {
    @OptIn(ExperimentalSerializationApi::class)
    protected val httpClient = HttpClient(NetworkUtils.defaultPlatformEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
                explicitNulls = false
            })
        }
    }
}
