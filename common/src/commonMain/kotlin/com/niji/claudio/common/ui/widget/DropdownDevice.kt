package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.ui.MediasViewModel


@Composable
fun DropdownDevice(mVm: MediasViewModel) {
    val deviceList = mVm.devicesState.collectAsState()
    val currentDevice = mVm.currentDevice.collectAsState()
    val selectedDevice = mVm.selectedDevice.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .background(MaterialTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            ) {
                ProgressIndicator(mVm.showOperationInProgress)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
            ) {
                Row(modifier = Modifier.padding(15.dp)) {
                    Text(
                        deviceList.value.find { it.serverId == selectedDevice.value?.serverId }?.name
                            ?: "No name",
                        color = MaterialTheme.colors.onBackground,
                        maxLines = 1
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "ArrowDropDown",
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
            ClaudioDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                deviceList.value.forEachIndexed { _, device ->
                    ClaudioDropdownMenuItem(onClick = {
                        mVm.onDeviceSelected(device)
                        expanded = false
                    }) {
                        val isSelected = device.serverId == selectedDevice.value?.serverId
                        val style = if (isSelected) {
                            MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary
                            )
                        } else {
                            MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                        Row {
                            Text(
                                text = device.name ?: "No name..",
                                style = style
                            )
                            if (currentDevice.value?.serverId != null
                                && currentDevice.value?.serverId == device.serverId
                            ) {
                                Text(
                                    text = "  It's you ;)",
                                    color = MaterialTheme.colors.secondary,
                                    style = style
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}