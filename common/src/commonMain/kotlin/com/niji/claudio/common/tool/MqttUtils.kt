package com.niji.claudio.common.tool

import com.niji.claudio.common.data.feature.log.usecase.AddDataLog
import com.niji.claudio.common.data.feature.media.usecase.GetMediaFromServerIdUseCase
import com.niji.claudio.common.data.model.ClaudioData
import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Media
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MqttUtils {

    private fun getActionTitle(data: ClaudioData) = when {
        data.tts != null -> "TTS"
        data.mediaPlay != null -> "Media play"
        data.volumeRaise != null -> "Volume raise"
        data.volumeLower != null -> "Volume lower"
        data.volumeMax != null -> "Volume max"
        data.volumeMin != null -> "Volume mute"
        data.vibrate != null -> "Vibrate"
        data.killPlayer != null -> "Kill player"
        else -> "Unknown event"
    }

    private suspend fun getNotifContent(data: ClaudioData): String {
        var content = ""
        data.tts?.let { content += "${it.message}\r\n" }
        data.mediaPlay?.let { content += getMediaData(it) }
        content += getFromMessage(data)
        return content
    }

    suspend fun createNotification(data: ClaudioData) {
        val title = getActionTitle(data) + " ignored"
        val content = getNotifContent(data)
        NotificationUtils.createNotification(title, content)
    }

    private fun getFromMessage(data: ClaudioData) =
        data.tts?.fromTitle ?: data.mediaPlay?.fromTitle ?: data.volumeRaise?.fromTitle
        ?: data.volumeLower?.fromTitle ?: data.volumeMax?.fromTitle ?: data.volumeMin?.fromTitle
        ?: data.killPlayer?.fromTitle ?: data.vibrate?.fromTitle ?: "Unknown"

    private suspend fun getMediaData(media: Media): String {
        return media.serverId?.let { GetMediaFromServerIdUseCase(it).execute()?.title } + "\r\n"
    }

    fun logMessage(data: ClaudioData, isIgnored: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            AddDataLog(
                DataLog(
                    dateStr = DateUtils.getCurrentDateStrType1(),
                    action = getActionTitle(data),
                    isIgnored = isIgnored,
                    data = getNotifContent(data).replace("\r\n", " / ")
                )
            ).execute()
        }
    }
}