package com.niji.claudio.common.tool

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import androidx.core.app.ActivityCompat
import com.niji.claudio.common.ClaudioApplication
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

actual object VoiceRecordService {

    private const val TAG = "VoiceRecordService"
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private val BUFFER_SIZE =
        AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

    private var audioRecord: AudioRecord? = null
    private var isRecording: Boolean = false

    actual fun startRecording() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            try {
                if (ActivityCompat.checkSelfPermission(
                        ClaudioApplication.applicationContext(),
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    LogUtils.d(TAG, "permission OK")
                    stopRecording()
                    audioRecord = AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT,
                        BUFFER_SIZE
                    )
                    val buffer = ByteArray(BUFFER_SIZE)
                    isRecording = true
                    audioRecord?.startRecording()
                    val user = GetUserUseCase().execute()
                    if (AcousticEchoCanceler.isAvailable()) {
                        val echoCanceler = audioRecord?.audioSessionId?.let {
                            AcousticEchoCanceler.create(it)
                        }
                        if (echoCanceler != null) {
                            echoCanceler.enabled = true
                        }
                    }
                    while (isRecording) {
                        val bytesRead = audioRecord?.read(buffer, 0, BUFFER_SIZE)
                        if (bytesRead != null && bytesRead > 0) {
                            val audioData = buffer.copyOf(bytesRead)
                            MqttClientProvider.publishAtMost(
                                "${user.selectedServerIdDevice}",
                                audioData
                            )
                        }
                    }
                } else {
                    LogUtils.e(TAG, "permission KO :(")
                }
            } catch (e: Exception) {
                LogUtils.e(TAG, "Exception caught in recordAudio :(", e)
            } finally {
                stopRecording()
            }
        }
    }

    actual fun stopRecording() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            isRecording = false
            try {
                audioRecord?.stop()
                audioRecord?.release()
            } catch (e: Exception) {
                LogUtils.e(TAG, "Exception caught in stop :(", e)
            } finally {
                audioRecord = null
            }
        }
    }
}