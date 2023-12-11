package com.niji.claudio.common.data.feature.user

import com.niji.claudio.common.data.IAbstractRepository
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.User

interface IUserRepository : IAbstractRepository {
    suspend fun getUser(): User
    suspend fun getSelectedDevice(): Device
    suspend fun getCurrentDevice(): Device?
    suspend fun setSelectedDevice(device: Device)
    suspend fun setUserName(name: String)
    suspend fun getMediaDisplayPreference(): String
    suspend fun setMediaDisplayPreference(state: String)
    suspend fun setSleepingMode(isSleeping: Boolean)
    suspend fun setUserToken(token: String)
    suspend fun syncUserDeviceIfPossible(mustUpdateDevice: Boolean = false)
    suspend fun setToggleAdminBtnClick(btn: String): Boolean
}