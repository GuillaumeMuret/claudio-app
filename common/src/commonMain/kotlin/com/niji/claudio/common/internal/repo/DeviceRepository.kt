package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.api.Resource
import com.niji.claudio.common.data.feature.device.IDeviceRepository
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.save.IDeviceDatabase


class DeviceRepository(private val api: IClaudioApi, private val database: IDeviceDatabase) : AbstractRepository(),
    IDeviceRepository {

    private var getDevicesCache: List<Device>? = null

    override suspend fun getDevices(): List<Device> {
        if (mustReload) {
            val responseResource = sendGeneric(api::getDevices)
            if (responseResource is Resource.Success) {
                responseResource.data?.let {
                    database.saveDevices(it)
                    getDevicesCache = it
                    mustReload = false
                }
            }
        }
        if (getDevicesCache == null) {
            getDevicesCache = database.getDevices()
        }
        return getDevicesCache ?: listOf()
    }

    override suspend fun addDevice(device: Device): Device? {
        var deviceToReturn: Device? = null
        val responseResource = sendGeneric { api.postDevice(device) }
        if (responseResource is Resource.Success) {
            responseResource.data?.let {
                deviceToReturn = device.apply {
                    serverId = it.serverId
                }
                mustReload = true
                getDevices()
            }
        }
        return deviceToReturn
    }

    override suspend fun deleteDevice(device: Device): Device? {
        val responseResource = sendGeneric { api.deleteDevice(device) }
        return if (responseResource is Resource.Success) {
            mustReload = true
            responseResource.data
        } else {
            null
        }
    }

    companion object {
        private const val TAG = "DeviceRepository"
    }
}