package com.niji.claudio.common.internal.repo.api

import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import kotlinx.coroutines.delay


class FakeClaudioApi : AbstractKtorClient(), IClaudioApi {

    val api = this

    override suspend fun getDevices(): List<Device> {
        delay(FAKE_REQUEST_DELAY)
        return FAKE_DEVICES
    }

    override suspend fun postDevice(device: Device): Device {
        delay(FAKE_REQUEST_DELAY)
        val newDevice = device.apply {
            serverId = FAKE_USER_DEVICE_SERVER_ID
        }
        FAKE_DEVICES.add(newDevice)
        return newDevice
    }

    override suspend fun deleteDevice(device: Device): Device {
        delay(FAKE_REQUEST_DELAY)
        FAKE_DEVICES.removeAll { it.serverId == device.serverId }
        return device
    }

    override suspend fun getMedias(): List<Media> {
        delay(FAKE_REQUEST_DELAY)
        return FAKE_MEDIAS
    }

    override suspend fun getMedia(id: String?): Media {
        return FAKE_MEDIAS.find { it.serverId == id }!!
    }

    override suspend fun deleteMedia(id: String?): List<Media> {
        delay(FAKE_REQUEST_DELAY)
        return FAKE_MEDIAS.apply {
            removeAll { it.serverId == id }
        }
    }

    override suspend fun downloadMedia(urlStr: String, media: Media) = media

    override suspend fun addMedia(media: Media) = media

    companion object {
        private const val FAKE_REQUEST_DELAY = 1000L
        private const val FAKE_USER_DEVICE_SERVER_ID = "user device server id"
        private val FAKE_DEVICES = mutableListOf(
            Device(
                serverId = "1 serverDeviceId",
                name = "Fake device name 1"
            ),
            Device(
                serverId = "2 serverDeviceId",
                name = "Fake device name 2"
            ),
            Device(
                serverId = "3 serverDeviceId",
                name = "Fake device name 3"
            ),
            Device(
                serverId = "4 serverDeviceId",
                name = "Fake device name 4"
            )
        )
        private val FAKE_MEDIAS = mutableListOf(
            Media(
                serverId = "serverMediaId 1",
                title = "Fake media title 1",
                filename = "Fake filename 1",
                createdAt = "0",
                size = 53467
            ),
            Media(
                serverId = "serverMediaId 2",
                title = "Fake media title 2",
                filename = "Fake filename 2",
                createdAt = "1",
                size = 23467
            ),
            Media(
                serverId = "serverMediaId 3",
                title = "Fake media title 3",
                filename = "Fake filename 3",
                createdAt = "2",
                size = 953467
            ),
            Media(
                serverId = "serverMediaId 4",
                title = "Fake media title 4",
                filename = "Fake filename 4",
                createdAt = "3",
                size = 66653467
            )
        )
    }
}
