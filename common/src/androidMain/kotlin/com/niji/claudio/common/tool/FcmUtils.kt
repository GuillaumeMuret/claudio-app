package com.niji.claudio.common.tool

import com.niji.claudio.common.ClaudioFcmService
import com.niji.claudio.common.data.feature.log.usecase.AddDataLog
import com.niji.claudio.common.data.feature.media.usecase.GetMediaFromServerIdUseCase
import com.niji.claudio.common.data.model.DataLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


object FcmUtils {

    private const val FROM_UNKNOWN = "From an unknown device :("

    enum class Action(val id: String, val notificationTitle: String) {
        FCM_DATA_KEY_TTS(ClaudioFcmService.FCM_DATA_KEY_TTS, "TTS"),
        FCM_DATA_KEY_MEDIA_PLAY(ClaudioFcmService.FCM_DATA_KEY_MEDIA_PLAY, "Media play"),
        FCM_DATA_KEY_VOLUME_RAISE(ClaudioFcmService.FCM_DATA_KEY_VOLUME_RAISE, "Volume raise"),
        FCM_DATA_KEY_VOLUME_LOWER(ClaudioFcmService.FCM_DATA_KEY_VOLUME_LOWER, "Volume lower"),
        FCM_DATA_KEY_VOLUME_MAX(ClaudioFcmService.FCM_DATA_KEY_VOLUME_MAX, "Volume max"),
        FCM_DATA_KEY_VOLUME_MIN(ClaudioFcmService.FCM_DATA_KEY_VOLUME_MIN, "Volume mute"),
        FCM_DATA_KEY_VIBRATE(ClaudioFcmService.FCM_DATA_KEY_VIBRATE, "Vibrate"),
        FCM_DATA_KEY_KILL_PLAYER(ClaudioFcmService.FCM_DATA_KEY_KILL_PLAYER, "Kill player"),
        FCM_DATA_UNKNOWN(ClaudioFcmService.FCM_DATA_KEY_UNKNOWN, "Unknown event");

        companion object {
            fun get(id: String) = Action.values().find { it.id == id } ?: FCM_DATA_UNKNOWN
            fun getIds() = Action.values().toList()
        }
    }

    private fun getActionTitle(data: Map<String, String>): String {
        var title = ""
        Action.getIds().forEach {
            if (data[it.id] != null) title += it.notificationTitle
        }
        return title
    }

    private suspend fun getNotifContent(data: Map<String, String>): String {
        var content = ""
        data[ClaudioFcmService.FCM_DATA_KEY_TTS]?.let { content += getTtsData(it) }
        data[ClaudioFcmService.FCM_DATA_KEY_MEDIA_PLAY]?.let { content += getMediaData(it) }
        data.values.forEach { content += getFromMessage(it) }
        return content
    }

    suspend fun createNotification(data: Map<String, String>) {
        val title = getActionTitle(data) + " ignored"
        val content = getNotifContent(data)
        NotificationUtils.createNotification(title, content)
    }

    private fun getFromMessage(json: String? = "{}"): String {
        val message = try {
            val jsonObj = json?.let { JSONObject(it) }
            "From " + jsonObj?.get("fromTitle").toString()
        } catch (e: JSONException) {
            FROM_UNKNOWN
        }
        return message
    }

    private fun getTtsData(json: String? = "{}"): String {
        val message = try {
            val jsonObj = json?.let { JSONObject(it) }
            jsonObj?.get("message").toString() + "\r\n"
        } catch (e: JSONException) {
            ""
        }
        return message
    }

    private suspend fun getMediaData(json: String? = "{}"): String {
        val message = try {
            val jsonObj = json?.let { JSONObject(it) }
            val id = jsonObj?.get("id").toString()
            GetMediaFromServerIdUseCase(id).execute()?.title + "\r\n"
        } catch (e: JSONException) {
            ""
        }
        return message
    }

    fun logMessage(data: Map<String, String>, isIgnored: Boolean) {
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