package com.niji.claudio.common.data.save

import com.niji.claudio.common.data.model.DataLog

interface IDataLogDatabase {
    suspend fun getDataLogs(): List<DataLog>
    suspend fun addDataLog(dataLog: DataLog)
}