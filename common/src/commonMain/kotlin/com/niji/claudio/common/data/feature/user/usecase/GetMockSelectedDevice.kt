package com.niji.claudio.common.data.feature.user.usecase

import com.niji.claudio.common.data.feature.device.usecase.GetMockDevicesUseCase
import com.niji.claudio.common.data.model.Device

class GetMockSelectedDevice {
    suspend fun execute(): Device {
        return GetMockDevicesUseCase.DEVICES[1]
    }
}