package com.niji.claudio.common.tool

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.HandlerThread
import java.io.IOException


actual object VoicePlayerService {

    private const val TAG = "VoicePlayerService"
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_OUT_MONO
    private const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT

    private var audioTrack: AudioTrack? = null

    private val handler: Handler
    private var buffer: ByteArray? = null
    private var isPlaying = false

    init {
        val trackThread = HandlerThread("TrackThread")
        trackThread.start()
        val trackLooper = trackThread.looper
        handler = Handler(trackLooper)
    }

    private fun initTrack() {
        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AUDIO_FORMAT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(CHANNEL_CONFIG)
                        .build()
                )
                .setBufferSizeInBytes(
                    AudioTrack.getMinBufferSize(
                        SAMPLE_RATE,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT
                    )
                )
                .build()
        } catch (e: Exception) {
            LogUtils.e(TAG, "Error initTrack :(", e)
        }
    }

    actual fun play(byteArray: ByteArray) {
        LogUtils.d(TAG, "Play it $byteArray / ${audioTrack?.playState}")
        if (audioTrack == null) {
            initTrack()
        }
        buffer = byteArray
        startPlaying()
    }

    private fun startPlaying() {
        if (!isPlaying && buffer != null) {
            isPlaying = true
            audioTrack?.play()
            audioTrack?.play()
        }
        handler.removeCallbacksAndMessages(null)
        handler.post(playRunnable)
    }

    private val playRunnable = Runnable {
        val bytesRead = buffer?.size ?: 0
        if (bytesRead > 0) {
            try {
                val bufferSizeInBytes = audioTrack?.write(buffer ?: ByteArray(0), 0, bytesRead)
                if (bufferSizeInBytes == AudioTrack.ERROR_BAD_VALUE || bufferSizeInBytes == AudioTrack.ERROR_INVALID_OPERATION) {
                    LogUtils.e(TAG, "Error writing audio data to track")
                    releaseAudioTrack()
                }
            } catch (e: IOException) {
                LogUtils.e(TAG, "Error playing stream :(", e)
                releaseAudioTrack()
            } catch (e: IllegalStateException) {
                LogUtils.e(TAG, "App state issue :(", e)
                releaseAudioTrack()
            }
        }
    }

    private fun releaseAudioTrack() {
        LogUtils.d(TAG, "releaseAudioTrack")
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        buffer = null
    }
}