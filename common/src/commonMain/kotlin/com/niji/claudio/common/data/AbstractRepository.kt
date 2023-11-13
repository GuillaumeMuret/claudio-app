package com.niji.claudio.common.data

import com.niji.claudio.common.data.api.DataSource
import com.niji.claudio.common.data.api.Resource
import com.niji.claudio.common.data.api.exception.ErrorRequestException
import com.niji.claudio.common.data.api.exception.OfflineException
import com.niji.claudio.common.tool.LogUtils
import io.ktor.utils.io.errors.IOException


abstract class AbstractRepository : IAbstractRepository {
    var mustReload = true
    protected suspend fun <T> sendGeneric(call: suspend () -> T): Resource<T> {
        return try {
            handleResult { call.invoke() }
        } catch (ioe: IOException) {
            LogUtils.e(TAG, "ioe $ioe")
            Resource.Error(OfflineException())
        } catch (e: Exception) {
            LogUtils.e(TAG, "e $e")
            Resource.Error(ErrorRequestException())
        }
    }

    private suspend fun <T> handleResult(call: suspend () -> T): Resource<T> {
        val response = call()
        return Resource.Success(response, DataSource.NETWORK)
    }

    override fun setMustReload() {
        mustReload = true
    }

    companion object {
        private const val TAG = "AbstractRepository"
    }
}