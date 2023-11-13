package com.niji.claudio.common.data.feature.device.usecase

import com.niji.claudio.common.data.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMockDevicesUseCase {
    fun execute(): Flow<List<Device>> {
        return flow {
            emit(DEVICES.sortedBy { it.name })
        }
    }

    companion object {
        val DEVICES = listOf(
            Device(
                bddId = 0,
                name = "device0",
                pushToken = "pushToken0",
                serverId = "serverId0",
            ),
            Device(
                bddId = 1,
                name = "device1",
                pushToken = "pushToken1",
                serverId = "serverId1",
            ),
            Device(
                bddId = 2,
                name = "device2",
                pushToken = "pushToken2",
                serverId = "serverId2",
            )
        )
    }
}