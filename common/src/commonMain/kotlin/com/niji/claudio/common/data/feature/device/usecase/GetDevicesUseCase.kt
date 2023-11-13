package com.niji.claudio.common.data.feature.device.usecase

import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.internal.RepositoryLocator


class GetDevicesUseCase {
    suspend fun execute(): List<Device> {
        val devices = RepositoryLocator.deviceRepository.getDevices().toMutableList()
        val currentDevice = RepositoryLocator.userRepository.getCurrentDevice()
        currentDevice?.let {
            if (devices.find { it.serverId == currentDevice.serverId } == null) {
                devices.add(0, currentDevice)
            }
        }
        return devices
    }
}