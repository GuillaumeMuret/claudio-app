package com.niji.claudio.common.data.save

import com.niji.claudio.common.data.model.Device

interface IDeviceDatabase {
    suspend fun getDevices(): List<Device>
    suspend fun saveDevices(devices: List<Device>)
    suspend fun deleteDevice(device: Device)
    suspend fun deleteAllDevice()
}