package com.niji.claudio.common.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
actual data class Media actual constructor(
    actual var bddId: Long?,
    actual var filePath: String?,
    actual var filename: String?,
    @SerialName("id")
    actual var serverId: String?,
    @SerialName("duration_sec")
    actual var durationSec: Float?,
    actual val url: String?,
    actual var title: String?,
    actual var category: String?,
    actual var isDownloaded: Boolean?,
    @SerialName("play_count")
    actual var playCount: Int?,
    actual var size: Int?,
    actual var fromTitle: String?,
    actual var isFavorite: Boolean?,
    @SerialName("created_at")
    actual var createdAt: String?,
) {
    @delegate:Transient
    actual var isDownloadedState by mutableStateOf(isDownloaded)

    @delegate:Transient
    actual var isFavoriteState by mutableStateOf(isFavorite)

    @delegate:Transient
    actual var downloadProgressState: DownloadProgress? by mutableStateOf(null)
}
