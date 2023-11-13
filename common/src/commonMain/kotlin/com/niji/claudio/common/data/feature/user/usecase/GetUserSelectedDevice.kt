package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.internal.RepositoryLocator


class GetUserSelectedDevice {
    suspend fun execute(): Device {
        return RepositoryLocator.userRepository.getSelectedDevice()
    }
}