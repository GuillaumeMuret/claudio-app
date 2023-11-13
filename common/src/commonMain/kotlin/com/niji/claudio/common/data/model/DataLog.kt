package com.niji.claudio.common.data.model

data class DataLog(
    var dateStr: String,
    var action: String? = null,
    var isIgnored: Boolean? = null,
    var data: String? = null
)