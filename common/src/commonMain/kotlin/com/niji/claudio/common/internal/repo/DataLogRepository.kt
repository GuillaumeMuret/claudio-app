package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.feature.log.IDataLogRepository
import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.save.IDataLogDatabase


class DataLogRepository(private val dataLogDatabase: IDataLogDatabase) : AbstractRepository(),
    IDataLogRepository {

    override suspend fun getDataLogs() = dataLogDatabase.getDataLogs()

    override suspend fun addDataLog(dataLog: DataLog) {
        dataLogDatabase.addDataLog(dataLog)
    }

    companion object {
        private const val TAG = "DataLog Repository"
    }
}