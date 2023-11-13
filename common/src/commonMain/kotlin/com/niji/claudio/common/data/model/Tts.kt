package com.niji.claudio.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Tts(
    var message: String = "Message erroné",
    val language: String = "fr",
    val pitch: Float = 1f,
    val speed: Float = 1f,
    val fromTitle: String? = null
)

