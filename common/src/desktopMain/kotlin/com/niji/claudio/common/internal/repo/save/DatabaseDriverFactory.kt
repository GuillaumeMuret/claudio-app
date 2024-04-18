package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.tool.LogUtils
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        checkDirectoryExist()
        val driver = JdbcSqliteDriver("jdbc:sqlite:${DATA_ROOT_PATH}/claudio.db")
        try {
            ClaudioDatabaseDelight.Schema.create(driver)
        } catch (e: Exception) {
            LogUtils.e(TAG, "Error in DatabaseDriverFactory", e)
        }
        ClaudioDatabaseDelight.Schema.migrate(
            driver = driver,
            oldVersion = 0,
            newVersion = ClaudioDatabaseDelight.Schema.version,
        )
        return driver
    }

    private fun checkDirectoryExist() {
        val directory = File(DATA_ROOT_PATH)
        if (!File(DATA_ROOT_PATH).exists()) {
            directory.mkdirs()
        }
    }

    companion object {
        private const val TAG = "DatabaseDriverFactory"
        private const val DATA_ROOT_PATH = "data"
    }
}
