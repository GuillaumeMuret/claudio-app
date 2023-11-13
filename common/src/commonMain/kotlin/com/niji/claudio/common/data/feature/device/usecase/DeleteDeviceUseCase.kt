package com.niji.claudio.common.data.feature.device.usecase

import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.internal.RepositoryLocator


class DeleteDeviceUseCase(val device: Device) {
    suspend fun execute(): List<Device> {
        RepositoryLocator.deviceRepository.deleteDevice(this.device)
        return GetDevicesUseCase().execute()
    }
}