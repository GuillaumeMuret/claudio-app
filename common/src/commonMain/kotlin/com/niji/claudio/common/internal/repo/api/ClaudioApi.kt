package com.niji.claudio.common.internal.repo.api

import com.niji.claudio.BuildKonfig
import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.DownloadProgress
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.tool.FileUtils
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.MediaUtils
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes


class ClaudioApi : AbstractKtorClient(), IClaudioApi {

    val api = this

    override suspend fun getDevices(): List<Device> =
        httpClient.get("${BuildKonfig.CLAUDIO_BASE_URL}/devices").body()

    override suspend fun postDevice(device: Device): Device =
        httpClient.post("${BuildKonfig.CLAUDIO_BASE_URL}/devices") {
            header("Content-Type", "application/json")
            setBody(Device(name = device.name, pushToken = device.pushToken))
        }.body()

    override suspend fun deleteDevice(device: Device): Device =
        httpClient.delete("${BuildKonfig.CLAUDIO_BASE_URL}/devices") {
            url { device.serverId?.let { parameters.append("id", it) } }
        }.body()

    override suspend fun getMedias(): List<Media> =
        httpClient.get("${BuildKonfig.CLAUDIO_BASE_URL}/medias").body()

    override suspend fun getMedia(id: String?): Media =
        httpClient.get("${BuildKonfig.CLAUDIO_BASE_URL}/media") {
            headers { append("x_claudio_auth_token", BuildKonfig.CLAUDIO_AUTH_TOKEN) }
            url { if (id != null) parameters.append("id", id) }
        }.body()

    override suspend fun deleteMedia(id: String?): List<Media> =
        httpClient.delete("${BuildKonfig.CLAUDIO_BASE_URL}/media") {
            url { if (id != null) parameters.append("id", id) }
        }.body()

    override suspend fun downloadMedia(urlStr: String, media: Media): Media {
        val filePath = FileUtils.getMediasDirectoryPath() + "/${media.filename}"
        val file = FileUtils.createMediaFile(filePath)
        media.filePath = filePath
        httpClient.prepareGet(urlStr).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    file.appendBytes(bytes)
                    media.downloadProgressState = DownloadProgress(
                        file.getLength().toInt(),
                        httpResponse.contentLength()?.toInt() ?: 0
                    )
                    LogUtils.d(
                        TAG,
                        "Received ${file.getLength()} bytes from ${httpResponse.contentLength()}"
                    )
                }
                packet.close()
            }
            media.isDownloadedState = true
            LogUtils.d(TAG, "A file saved to ${file.getPath()}")
        }
        LogUtils.d(TAG, "Media downloaded -> $media")
        return media
    }

    override suspend fun addMedia(media: Media): Media {
        LogUtils.d(TAG, "addMedia -> $media")
        val re = Regex("[^A-Za-z\\d.]")
        val encodedFileName = re.replace(media.filename.toString(), "")
        val headers =
            Headers.build { append(HttpHeaders.ContentDisposition, "filename=$encodedFileName") }
        LogUtils.d(TAG, "addMedia -> filename -> $encodedFileName")
        return httpClient.submitFormWithBinaryData(
            url = "${BuildKonfig.CLAUDIO_BASE_URL}/media",
            formData = formData {
                append("title", "${media.title}")
                append("filename", encodedFileName)
                append("category", "${media.category}")
                MediaUtils.getFileByteArray(media)?.let {
                    LogUtils.d(TAG, "addMedia -> readBytes")
                    append("file", it, headers)
                }
            }
        ).body()
    }

    companion object {
        private const val TAG = "ClaudioApi"
        private const val DEFAULT_BUFFER_SIZE = 1024
    }
}
