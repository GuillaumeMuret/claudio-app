package com.niji.claudio.common.data.feature.device

import com.niji.claudio.common.data.IAbstractRepository
import com.niji.claudio.common.data.model.Device


interface IDeviceRepository : IAbstractRepository {
    suspend fun getDevices(): List<Device>
    suspend fun addDevice(device: Device): Device?
    suspend fun deleteDevice(device: Device): Device?
}