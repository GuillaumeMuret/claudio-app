package com.niji.claudio.common.data

import com.niji.claudio.common.data.feature.device.IDeviceRepository
import com.niji.claudio.common.data.feature.hook.IHookRepository
import com.niji.claudio.common.data.feature.log.IDataLogRepository
import com.niji.claudio.common.data.feature.media.IMediaRepository
import com.niji.claudio.common.data.feature.player.IPlayerRepository
import com.niji.claudio.common.data.feature.user.IUserRepository


interface IRepositoryLocator {
    val deviceRepository: IDeviceRepository
    val mediaRepository: IMediaRepository
    val userRepository: IUserRepository
    val playerRepository: IPlayerRepository
    val dataLogRepository: IDataLogRepository
    val hookRepository: IHookRepository
}