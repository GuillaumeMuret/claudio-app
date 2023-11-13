package com.niji.claudio.common.data.api

import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media

interface IClaudioApi {
    // Device
    suspend fun getDevices(): List<Device>
    suspend fun postDevice(device: Device): Device
    suspend fun deleteDevice(device: Device): Device

    // Media
    suspend fun getMedias(): List<Media>
    suspend fun getMedia(id: String? = ""): Media
    suspend fun deleteMedia(id: String? = ""): List<Media>
    suspend fun downloadMedia(urlStr: String, media: Media): Media
    suspend fun addMedia(media: Media): Media
}