package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.theme.indigo
import com.niji.claudio.common.ui.theme.red


@Composable
fun DeviceList(mVm: MediasViewModel) {
    val itemSize = 80
    val deviceList = mVm.devicesState.collectAsState()
    val currentDevice = mVm.currentDevice.collectAsState()
    val selectedDevice = mVm.selectedDevice.collectAsState()
    val isAdminState = mVm.isAdminState.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize(), content = {
        LazyColumn(content = {
            items(items = deviceList.value, itemContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { mVm.onDeviceSelected(it) })
                        .padding(horizontal = 16.dp, vertical = 8.dp), content = {
                        val isItMe = it.serverId == currentDevice.value?.serverId
                        val color = if (selectedDevice.value?.serverId == it.serverId) {
                            MaterialTheme.colors.red
                        } else if (isItMe) {
                            MaterialTheme.colors.indigo
                        } else {
                            MaterialTheme.colors.onBackground
                        }
                        Box(
                            content = {
                                Text(
                                    text = if (isItMe) {
                                        "Me"
                                    } else {
                                        if (it.serverId.toString().length > 3) {
                                            it.serverId.toString().substring(0, 3)
                                        } else {
                                            it.serverId.toString()
                                        }
                                    },
                                    fontSize = 24.sp,
                                    color = color
                                )
                            }, modifier = Modifier.size(itemSize.dp).border(
                                width = 1.2.dp,
                                color = color,
                                shape = CircleShape
                            ), contentAlignment = Alignment.Center
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(2F), content = {
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = it.name ?: "",
                                color = color,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            it.serverId?.let { serverId ->
                                Text(
                                    text = serverId, color = color, fontSize = 14.6.sp
                                )
                            }
                            Text(
                                text = it.pushToken ?: "No token :(",
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        })
                        Spacer(modifier = Modifier.size(16.dp))
                        if (isAdminState.value) {
                            IconButton(onClick = { mVm.deleteDevice(it) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colors.red,
                                    modifier = Modifier.width(30.dp).height(itemSize.dp)
                                )
                            }
                        }
                    })
            })
        })
    })
}