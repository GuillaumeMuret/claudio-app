package com.niji.claudio.common.ui.widget.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.niji.claudio.common.resources.Res
import com.niji.claudio.common.resources.ic_toggle_display
import com.niji.claudio.common.ui.ClaudioScreens
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.theme.toolbarBackground
import org.jetbrains.compose.resources.painterResource


@Composable
fun MediasToolbar(mVm: MediasViewModel, navController: NavHostController) {
    val isFavoriteMode = mVm.isFavoriteMode.collectAsState()
    GenericToolbar(
        title = "Medias",
        actions = {
            IconButton(onClick = { mVm.addMedia() }) {
                Icon(Icons.Filled.Add, "Add")
            }
            IconButton(onClick = { mVm.toggleFavoriteMode() }) {
                Icon(
                    imageVector = if (isFavoriteMode.value) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = "Favorite"
                )
            }
            IconButton(onClick = { mVm.refreshMedia() }) {
                Icon(Icons.Filled.Refresh, "Refresh")
            }
            IconButton(onClick = { mVm.toggleDisplay() }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_toggle_display),
                    contentDescription = "Toggle display"
                )
            }
            IconButton(onClick = { navController.navigate(ClaudioScreens.Device.name) }) {
                Icon(Icons.Filled.Settings, null)
            }
        }
    )
}

@Composable
fun DevicesToolbar(mVm: MediasViewModel, navController: NavHostController) {
    GenericToolbar(title = "Devices",
        actions = {
            IconButton(onClick = { mVm.refreshDevices() }) {
                Icon(Icons.Filled.Refresh, "Refresh")
            }
            IconButton(onClick = { navController.navigate(ClaudioScreens.DataLog.name) }) {
                Icon(Icons.AutoMirrored.Filled.List, "Logs")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }
    )
}

@Composable
fun DataLogToolbar(mVm: MediasViewModel, navController: NavHostController) {
    GenericToolbar(title = "Data Logs",
        actions = {
            IconButton(onClick = { mVm.refreshDataLogs() }) {
                Icon(Icons.Filled.Refresh, "Refresh")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }
    )
}

@Composable
fun GenericToolbar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = MaterialTheme.colors.toolbarBackground,
        contentColor = Color.White
    )
}
