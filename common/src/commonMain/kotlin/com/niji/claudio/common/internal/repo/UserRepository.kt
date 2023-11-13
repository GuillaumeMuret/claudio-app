package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.feature.device.IDeviceRepository
import com.niji.claudio.common.data.feature.device.usecase.GetDevicesUseCase
import com.niji.claudio.common.data.feature.user.IUserRepository
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IUserDatabase
import com.niji.claudio.common.tool.LogUtils


class UserRepository(
    private val api: IClaudioApi,
    private val userDatabase: IUserDatabase,
    private val deviceRepository: IDeviceRepository
) : AbstractRepository(), IUserRepository {

    private var userCache: User? = null
    private var indexToggleAdmin = 0

    override suspend fun getUser(): User {
        if (mustReload || userCache == null) {
            userCache = userDatabase.getUser()
            mustReload = false
        }
        return userCache ?: User()
    }

    private suspend fun updateUser(user: User) {
        userCache = user
        userDatabase.updateUser(user)
    }

    override suspend fun getSelectedDevice(): Device {
        val user = getUser()
        val devices = GetDevicesUseCase().execute()
        return devices.find { it.serverId == user.selectedServerIdDevice }
            ?: devices.getOrNull(0)
            ?: Device()
    }

    override suspend fun getCurrentDevice(): Device? {
        val user = getUser()
        return deviceRepository.getDevices()
            .find { user.mDeviceServerId == it.serverId }
            ?: user.name?.let { Device.noTokenCurrentDevice(it) }
    }

    override suspend fun setSelectedDevice(device: Device) {
        val user = getUser()
        user.selectedServerIdDevice = device.serverId
        updateUser(user)
    }

    override suspend fun setUserName(name: String) {
        val user = getUser()
        user.name = name
        updateUser(user)
    }

    override suspend fun getMediaDisplayPreference(): String {
        return getUser().mediaDisplayPreference ?: ""
    }

    override suspend fun setMediaDisplayPreference(state: String) {
        val user = getUser()
        user.mediaDisplayPreference = state
        updateUser(user)
    }

    override suspend fun setSleepingMode(isSleeping: Boolean) {
        val user = getUser()
        user.isSleepingMode = isSleeping
        updateUser(user)
    }

    override suspend fun setUserToken(token: String) {
        val user = getUser()
        user.mPushToken = token
        updateUser(user)
    }

    private suspend fun toggleAdmin() {
        LogUtils.d(TAG, "Toggle Admin")
        val user = getUser()
        user.isAdmin = user.isAdmin == null || user.isAdmin == false
        updateUser(user)
    }

    override suspend fun setBtnClick(btn: String): Boolean {
        var mustReload = false
        if (SEQUENCE_TOGGLE_ADMIN.getOrNull(indexToggleAdmin) == btn) {
            ++indexToggleAdmin
            if (indexToggleAdmin == SEQUENCE_TOGGLE_ADMIN.size) {
                mustReload = true
                toggleAdmin()
                indexToggleAdmin = 0
            }
        } else {
            indexToggleAdmin = 0
        }
        return mustReload
    }

    override suspend fun syncUserDeviceIfPossible(mustUpdateDevice: Boolean) {
        val user = getUser()
        val name = user.name
        val serverId = user.mDeviceServerId
        val pushToken = user.mPushToken
        LogUtils.d(TAG, "pushToken = $pushToken, name = $name, user = $user, serverId = $serverId")
        if (!name.isNullOrEmpty()) {
            if (serverId == null || serverId == Device.NO_SERVER_ID) {
                val device = deviceRepository.addDevice(
                    Device(
                        name = name,
                        pushToken = pushToken ?: Device.NO_PUSH_TOKEN
                    )
                )
                device?.serverId?.let { user.mDeviceServerId = it }
            } else {
                if (mustUpdateDevice) {
                    // TODO make update deviceRepository.updateDevice(Device(serverId = serverId, name = name, pushToken = pushToken))
                    getCurrentDevice()?.let { currentDevice ->
                        val list = deviceRepository.deleteDevice(currentDevice)
                        LogUtils.d(TAG, "Deleted device")
                        if (list != null) {
                            val device = deviceRepository.addDevice(
                                Device(
                                    name = name,
                                    pushToken = pushToken
                                )
                            )
                            LogUtils.d(TAG, "Add new device")
                            device?.serverId?.let { user.mDeviceServerId = it }
                        }
                    }
                    // End section to update
                }
            }
        }
        updateUser(user)
    }

    companion object {
        private const val TAG = "UserRepository"
        private val SEQUENCE_TOGGLE_ADMIN = listOf(
            "Kill them all",
            "Kill them all",
            "Kill them all",
            "Volume mute",
            "Volume mute",
            "Volume down",
            "Kill them all",
            "Kill them all",
            "Kill them all",
            "Volume mute",
            "Volume mute",
            "Volume down"
        )
    }
}