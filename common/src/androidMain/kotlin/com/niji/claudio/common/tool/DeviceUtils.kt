package com.niji.claudio.common.tool

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.niji.claudio.common.ClaudioApplication

actual object DeviceUtils {

    private const val VIBRATION_TIME_MS = 500L
    private const val AUDIO_MANAGER_FLAGS = AudioManager.FLAG_PLAY_SOUND

    @Suppress("DEPRECATION")
    actual fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = ClaudioApplication.applicationContext()
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
            val vibrator = vibratorManager?.defaultVibrator
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATION_TIME_MS,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val v = ClaudioApplication.applicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
            v?.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATION_TIME_MS,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            val v = ClaudioApplication.applicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
            v?.vibrate(VIBRATION_TIME_MS)
        }
    }


    private fun getAudioManager() = ClaudioApplication.applicationContext()
        .getSystemService(Context.AUDIO_SERVICE) as AudioManager

    actual fun raiseVolume() {
        getAudioManager().adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_RAISE,
            AUDIO_MANAGER_FLAGS
        )
    }

    actual fun lowerVolume() {
        getAudioManager().adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER,
            AUDIO_MANAGER_FLAGS
        )
    }

    actual fun maxVolume() {
        getAudioManager().setStreamVolume(
            AudioManager.STREAM_MUSIC,
            getAudioManager().getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            AUDIO_MANAGER_FLAGS
        )
    }

    actual fun minVolume() {
        getAudioManager().setStreamVolume(
            AudioManager.STREAM_MUSIC,
            0,
            AUDIO_MANAGER_FLAGS
        )
    }
}