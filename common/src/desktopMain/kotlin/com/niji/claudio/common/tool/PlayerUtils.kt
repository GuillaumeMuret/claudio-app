package com.niji.claudio.common.tool

import com.niji.claudio.common.data.feature.media.usecase.GetMediaFromServerIdUseCase
import com.niji.claudio.common.data.feature.player.usecase.PlayMediaLocalUseCase
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts
import java.awt.Desktop
import java.io.File

actual object PlayerUtils {

    private const val TAG = "PlayerUtils"

    actual fun play(media: Media) {
        Desktop.getDesktop().open(media.filePath?.let { File(it) })
    }

    actual fun playTts(ttsStr: String) {
        // play from FCM so do nothing.
    }

    actual fun playTts(tts: Tts) {
        // TODO
    }

    actual fun killPlayer() {
        // TODO
    }

    actual suspend fun displayPlayerIfPossible(mediaToPlay: Media) {
        LogUtils.d(TAG, "displayPlayerIfPossible $mediaToPlay")
        val media = mediaToPlay.serverId?.let {
            LogUtils.d(TAG, "displayPlayerIfPossible $it")
            GetMediaFromServerIdUseCase(it).execute()
        }
        LogUtils.d(TAG, "displayPlayerIfPossible $media")
        media?.let {
            PlayMediaLocalUseCase(it).execute()
        }
    }
}