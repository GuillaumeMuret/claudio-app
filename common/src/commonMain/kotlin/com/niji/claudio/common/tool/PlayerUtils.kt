package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.Tts

expect object PlayerUtils {
    fun play(media: Media)
    fun playTts(ttsStr: String)
    fun playTts(tts: Tts)
    fun killPlayer()
    suspend fun displayPlayerIfPossible(mediaToPlay: Media)
}