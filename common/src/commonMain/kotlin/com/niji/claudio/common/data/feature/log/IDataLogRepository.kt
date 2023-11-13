package com.niji.claudio.common.data.feature.log

import com.niji.claudio.common.data.IAbstractRepository
import com.niji.claudio.common.data.model.DataLog


interface IDataLogRepository : IAbstractRepository {
    suspend fun getDataLogs(): List<DataLog>
    suspend fun addDataLog(dataLog: DataLog)
}
