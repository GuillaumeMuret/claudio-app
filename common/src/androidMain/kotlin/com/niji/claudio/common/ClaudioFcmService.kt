package com.niji.claudio.common

import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.niji.claudio.common.data.feature.hook.usecase.HookTokenUseCase
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import com.niji.claudio.common.data.feature.user.usecase.TokenReceivedUseCase
import com.niji.claudio.common.data.model.Slack
import com.niji.claudio.common.tool.DeviceUtils
import com.niji.claudio.common.tool.FcmUtils
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.PlayerUtils
import com.niji.claudio.common.tool.UserPreferencesUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClaudioFcmService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        LogUtils.d(TAG, "From: ${remoteMessage.from}")
        CoroutineScope(Dispatchers.Default).launch {
            val user = GetUserUseCase().execute()
            if (remoteMessage.data.isNotEmpty()) {
                LogUtils.d(TAG, "Message data payload: ${remoteMessage.data}")
                if (user.isSleepingMode == true) {
                    FcmUtils.createNotification(remoteMessage.data)
                } else {
                    remoteMessage.data[FCM_DATA_KEY_TTS]?.let { PlayerUtils.playTts(it) }
                    remoteMessage.data[FCM_DATA_KEY_MEDIA_PLAY]?.let { PlayerUtils.displayPlayerIfPossible(it) }
                    remoteMessage.data[FCM_DATA_KEY_VOLUME_RAISE]?.let { DeviceUtils.raiseVolume() }
                    remoteMessage.data[FCM_DATA_KEY_VOLUME_LOWER]?.let { DeviceUtils.lowerVolume() }
                    remoteMessage.data[FCM_DATA_KEY_VOLUME_MAX]?.let { DeviceUtils.maxVolume() }
                    remoteMessage.data[FCM_DATA_KEY_VOLUME_MIN]?.let { DeviceUtils.minVolume() }
                    remoteMessage.data[FCM_DATA_KEY_KILL_PLAYER]?.let { PlayerUtils.killPlayer() }
                    remoteMessage.data[FCM_DATA_KEY_VIBRATE]?.let { DeviceUtils.vibrate() }
                    remoteMessage.notification?.let {
                        LogUtils.d(TAG, "Message Notification Body: ${it.body}")
                    }
                }
                FcmUtils.logMessage(remoteMessage.data, user.isSleepingMode == true)
            }
        }
    }

    override fun onNewToken(token: String) {
        LogUtils.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        LogUtils.d(TAG, "sendRegistrationTokenToServer($token)")
        CoroutineScope(Dispatchers.IO).launch {
            token?.let { pushToken ->
                UserPreferencesUtils.put(applicationContext, UserPreferencesUtils.DEVICE_PUSH_TOKEN_PREF, token)
                TokenReceivedUseCase(pushToken).execute()
                try {
                    val response = HookTokenUseCase(
                        Slack(
                            "New Token = ${token}\n" +
                                    "VERSION = ${Build.VERSION.SDK_INT}\n" +
                                    "DEVICE = ${Build.DEVICE}\n" +
                                    "MODEL = ${Build.MODEL}\n" +
                                    "PRODUCT = ${Build.PRODUCT}",
                        )
                    ).execute()
                    LogUtils.d(TAG, "Response success -> $response")
                } catch (e: Exception) {
                    LogUtils.e(TAG, "Response issue", e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "ClaudioFcmService"
        const val FCM_DATA_KEY_TTS = "tts"
        const val FCM_DATA_KEY_MEDIA_PLAY = "media_play"
        const val FCM_DATA_KEY_VOLUME_RAISE = "volume_raise"
        const val FCM_DATA_KEY_VOLUME_LOWER = "volume_lower"
        const val FCM_DATA_KEY_VOLUME_MAX = "volume_max"
        const val FCM_DATA_KEY_VOLUME_MIN = "volume_min"
        const val FCM_DATA_KEY_KILL_PLAYER = "kill_player"
        const val FCM_DATA_KEY_VIBRATE = "vibrate"
        const val FCM_DATA_KEY_UNKNOWN = "fcm_data_key_unknown"
    }
}