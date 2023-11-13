package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.internal.RepositoryLocator


class SetSelectedDeviceUseCase(val device: Device) {
    suspend fun execute() {
        return RepositoryLocator.userRepository.setSelectedDevice(device)
    }
}