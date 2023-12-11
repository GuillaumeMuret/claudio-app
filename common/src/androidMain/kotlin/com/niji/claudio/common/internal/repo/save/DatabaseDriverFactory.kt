package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.ClaudioApplication
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            ClaudioDatabaseDelight.Schema,
            ClaudioApplication.applicationContext(),
            DatabaseVocabulary.DATABASE_NAME
        )
    }
}
