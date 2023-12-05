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
import com.niji.claudio.common.internal.repo.DataLogRepository
import com.niji.claudio.common.internal.repo.DeviceRepository
import com.niji.claudio.common.internal.repo.HookRepository
import com.niji.claudio.common.internal.repo.MediaRepository
import com.niji.claudio.common.internal.repo.MediaRepositoryFiles
import com.niji.claudio.common.internal.repo.PlayerRepository
import com.niji.claudio.common.internal.repo.PlayerRepositoryMqtt
import com.niji.claudio.common.internal.repo.UserRepository
import com.niji.claudio.common.internal.repo.api.ClaudioApi
import com.niji.claudio.common.internal.repo.api.FcmApi
import com.niji.claudio.common.internal.repo.api.SlackApi
import com.niji.claudio.common.internal.repo.save.ClaudioDatabase
import com.niji.claudio.common.internal.repo.save.Database
import com.niji.claudio.common.internal.repo.save.DatabaseDriverFactory

object RepositoryLocator : IRepositoryLocator {

    private val claudioApi: IClaudioApi = ClaudioApi().api
    private val playerApi: IPlayerApi = FcmApi().api
    private val hookApi: IHookApi = SlackApi().api
    private val claudioDatabase: IClaudioDatabase = ClaudioDatabase()
    private val database: IClaudioDatabase = Database(DatabaseDriverFactory())

    override val deviceRepository: IDeviceRepository = DeviceRepository(claudioApi, claudioDatabase)
    // val mediaRepository: IMediaRepository = MediaRepositoryFiles(claudioApi, database)
    override val mediaRepository: IMediaRepository = MediaRepository(claudioApi, database)
    override val userRepository: IUserRepository =
        UserRepository(claudioApi, claudioDatabase, deviceRepository)
    override val playerRepository: IPlayerRepository = if (BuildKonfig.IS_USING_FCM) {
        PlayerRepository(playerApi, userRepository)
    } else {
        PlayerRepositoryMqtt(userRepository)
    }
    override val dataLogRepository: IDataLogRepository = DataLogRepository(claudioDatabase)
    override val hookRepository: IHookRepository = HookRepository(hookApi)
}
