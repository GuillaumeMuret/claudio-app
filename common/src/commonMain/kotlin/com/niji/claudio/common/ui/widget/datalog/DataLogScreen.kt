package com.niji.claudio.common.ui.widget.datalog

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.widget.base.DataLogToolbar


@Composable
fun DataLogScreen(mVm: MediasViewModel, navController: NavHostController) {
    mVm.refreshDataLogs()
    Column {
        DataLogToolbar(mVm, navController)
        DataLogList(mVm)
    }
}
