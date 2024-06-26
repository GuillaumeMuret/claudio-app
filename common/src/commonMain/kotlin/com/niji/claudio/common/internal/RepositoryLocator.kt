package com.niji.claudio.common.internal

import com.niji.claudio.BuildKonfig
import com.niji.claudio.common.data.IRepositoryLocator
import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.api.IHookApi
import com.niji.claudio.common.data.api.IPlayerApi
import com.niji.claudio.common.data.feature.device.IDeviceRepository
import com.niji.claudio.common.data.feature.hook.IHookRepository
import com.niji.claudio.common.data.feature.log.IDataLogRepository
import com.niji.claudio.common.data.feature.media.IMediaRepository
import com.niji.claudio.common.data.feature.player.IPlayerRepository
import com.niji.claudio.common.data.feature.user.IUserRepository
import com.niji.claudio.common.data.save.IClaudioDatabase
import com.niji.claudio.common.getPlatform
import com.niji.claudio.common.internal.repo.DataLogRepository
import com.niji.claudio.common.internal.repo.DeviceRepository
import com.niji.claudio.common.internal.repo.HookRepository
import com.niji.claudio.common.internal.repo.MediaRepository
import com.niji.claudio.common.internal.repo.PlayerRepository
import com.niji.claudio.common.internal.repo.PlayerRepositoryMqtt
import com.niji.claudio.common.internal.repo.UserRepository
import com.niji.claudio.common.internal.repo.api.ClaudioApi
import com.niji.claudio.common.internal.repo.api.FakeClaudioApi
import com.niji.claudio.common.internal.repo.api.FcmApi
import com.niji.claudio.common.internal.repo.api.SlackApi
import com.niji.claudio.common.internal.repo.save.Database
import com.niji.claudio.common.internal.repo.save.DatabaseDriverFactory
import com.niji.claudio.common.internal.repo.save.VolatileDatabase
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.ui.state.Platform

object RepositoryLocator : IRepositoryLocator {

    private const val TAG = "RepositoryLocator"
    private const val FAKE_CLAUDIO_URL = "https://my-claudio-base-url.io"

    private val claudioApi: IClaudioApi = if (BuildKonfig.CLAUDIO_BASE_URL == FAKE_CLAUDIO_URL) {
        FakeClaudioApi().api
    } else {
        ClaudioApi().api
    }
    private val playerApi: IPlayerApi = FcmApi().api
    private val hookApi: IHookApi = SlackApi().api
    private val database: IClaudioDatabase = if (getPlatform() is Platform.Web) {
        LogUtils.d(TAG, "Init VolatileDatabase")
        VolatileDatabase()
    } else {
        LogUtils.d(TAG, "Init DatabaseDriverFactory")
        Database(DatabaseDriverFactory())
    }
    override val mediaRepository: IMediaRepository = MediaRepository(claudioApi, database)
    override val deviceRepository: IDeviceRepository = DeviceRepository(claudioApi, database)
    override val userRepository: IUserRepository =
        UserRepository(claudioApi, database, deviceRepository)
    override val playerRepository: IPlayerRepository = if (BuildKonfig.IS_USING_FCM) {
        PlayerRepository(playerApi, userRepository)
    } else {
        PlayerRepositoryMqtt(userRepository)
    }
    override val dataLogRepository: IDataLogRepository = DataLogRepository(database)
    override val hookRepository: IHookRepository = HookRepository(hookApi)
}
