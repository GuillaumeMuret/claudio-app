package com.niji.claudio.common.data.feature.log.usecase

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.internal.RepositoryLocator


class AddDataLog(val dataLog: DataLog) {
    suspend fun execute() = RepositoryLocator.dataLogRepository.addDataLog(dataLog)
}