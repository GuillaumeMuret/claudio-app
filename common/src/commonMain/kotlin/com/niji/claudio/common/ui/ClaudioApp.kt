package com.niji.claudio.common.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.niji.claudio.common.ui.theme.ClaudioTheme
import com.niji.claudio.common.ui.widget.datalog.DataLogScreen
import com.niji.claudio.common.ui.widget.device.DevicesScreen
import com.niji.claudio.common.ui.widget.dialog.DialogAddMedia
import com.niji.claudio.common.ui.widget.dialog.DialogError
import com.niji.claudio.common.ui.widget.dialog.DialogOverApp
import com.niji.claudio.common.ui.widget.dialog.DialogUserName
import com.niji.claudio.common.ui.widget.media.MediasScreen

enum class ClaudioScreens {
    Home,
    Device,
    DataLog,
}

@Composable
fun ClaudioApp(
    mVm: MediasViewModel = viewModel { MediasViewModel() },
    window: Any? = null,
    launchFileChooserIntent: (() -> Unit)? = null,
    navController: NavHostController = rememberNavController()
) {
    val showDeviceDialogState: Boolean by mVm.showDeviceDialogState.collectAsState()
    val showOverAppDialogState: Boolean by mVm.showOverAppDialogState.collectAsState()
    val showErrorDialogState: Boolean by mVm.showErrorDialogState.collectAsState()
    val localFocusManager = LocalFocusManager.current
    mVm.initMediaScreen()
    ClaudioTheme {
        Scaffold(contentWindowInsets = WindowInsets.statusBars) {
            Surface(color = MaterialTheme.colors.background) {
                DialogError(show = showErrorDialogState, onDismiss = mVm::onErrorDialogDismiss)
                DialogOverApp(
                    show = showOverAppDialogState,
                    onDismiss = mVm::onOverAppDialogDismiss,
                    onConfirm = mVm::onOverAppDialogConfirm
                )
                DialogUserName(
                    show = showDeviceDialogState,
                    onDismiss = mVm::onDeviceDialogDismiss,
                    onConfirm = mVm::onDeviceDialogConfirm,
                    mVm = mVm
                )
                DialogAddMedia(
                    mVm = mVm,
                    window = window,
                    launchFileChooserIntent = launchFileChooserIntent
                )
                NavHost(
                    navController = navController,
                    startDestination = ClaudioScreens.Home.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                localFocusManager.clearFocus()
                            })
                        }
                ) {
                    composable(route = ClaudioScreens.Home.name) {
                        MediasScreen(mVm, navController)
                    }
                    composable(route = ClaudioScreens.Device.name) {
                        DevicesScreen(mVm, navController)
                    }
                    composable(route = ClaudioScreens.DataLog.name) {
                        DataLogScreen(mVm, navController)
                    }
                }
            }
        }
    }
}
