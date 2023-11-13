package com.niji.claudio.common.tool

import java.text.SimpleDateFormat
import java.util.*

actual object DateUtils {

    actual fun getCurrentDateStrType1(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
}