package com.niji.claudio.common.tool

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.niji.claudio.common.ClaudioApplication
import com.niji.claudio.common.PlayerActivity
import com.niji.claudio.common.data.feature.media.usecase.GetMediaFromServerIdUseCase
import com.niji.claudio.common.data.feature.player.usecase.PlayMediaLocalUseCase
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

actual object PlayerUtils {
    private const val TAG = "PlayerManager"
    private val mediaPlayerList = mutableListOf<MediaPlayer>()
    private val gson = Gson()
    val killPlayerLiveData = MutableLiveData<Boolean>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            killPlayerLiveData.value = false
        }
    }

    private fun displayPlayer(filePath: String) {
        val intent = PlayerActivity.getIntent(ClaudioApplication.applicationContext(), filePath)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ClaudioApplication.applicationContext().startActivity(intent)
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

    fun displayPlayerIfPossible(mediaToPlayStr: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val mediaToPlay = gson.fromJson(mediaToPlayStr, Media::class.java)
            displayPlayerIfPossible(mediaToPlay)
        }
    }

    actual fun play(media: Media) {
        media.filePath?.let { displayPlayer(it) }
    }

    actual fun playTts(ttsStr: String) {
        playTts(Gson().fromJson(ttsStr, Tts::class.java))
    }

    actual fun playTts(tts: Tts) {
        LogUtils.d(TAG, "Play tts -> $tts")
        val audioUrl =
            "https://translate.google.com/translate_tts?ie=UTF-8" +
                    "&total=1" +
                    "&idx=0" +
                    "&tk=350535.255567" +
                    "&client=webapp" +
                    "&prev=input" +
                    "&q=${tts.message}" +
                    "&tl=${tts.language}"
        val mediaPlayer = MediaPlayer()
        mediaPlayer.let { safeMediaPlayer ->
            safeMediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            safeMediaPlayer.setOnPreparedListener {
                val params: PlaybackParams = safeMediaPlayer.playbackParams
                tts.pitch.let { params.pitch = it }
                tts.speed.let { params.speed = it }
                safeMediaPlayer.playbackParams = params
            }
            try {
                safeMediaPlayer.setDataSource(audioUrl)
                safeMediaPlayer.prepare()
                safeMediaPlayer.start()
                mediaPlayerList.add(safeMediaPlayer)
                safeMediaPlayer.setOnCompletionListener {
                    mediaPlayerList.remove(it)
                }
            } catch (e: IOException) {
                LogUtils.e(TAG, "Error in play sound ${e.message}", e)
            }
        }
    }

    actual fun killPlayer() {
        mediaPlayerList.forEach {
            it.stop()
        }
        mediaPlayerList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            LogUtils.d(TAG, "CoroutineScope killPlayer")
            killPlayerLiveData.value = true
            delay(500)
            killPlayerLiveData.value = false
        }
    }
}