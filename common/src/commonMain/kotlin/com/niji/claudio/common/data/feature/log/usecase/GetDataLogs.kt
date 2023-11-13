package com.niji.claudio.common.data.feature.log.usecase

import com.niji.claudio.common.internal.RepositoryLocator


class GetDataLogs() {
    suspend fun execute() = RepositoryLocator.dataLogRepository.getDataLogs()
}