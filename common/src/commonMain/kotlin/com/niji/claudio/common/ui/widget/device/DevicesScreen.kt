package com.niji.claudio.common.ui.widget.device

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.widget.base.DevicesToolbar


@Composable
fun DevicesScreen(mVm: MediasViewModel, navController: NavHostController) {
    Column {
        DevicesToolbar(mVm, navController)
        DeviceList(mVm)
    }
}
