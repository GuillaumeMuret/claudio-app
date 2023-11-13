package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niji.claudio.common.ui.MediasViewModel


@Composable
fun DataLogList(mVm: MediasViewModel) {
    val dataLogList = mVm.dataLogsState.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize(), content = {
        LazyColumn(content = {
            items(items = dataLogList.value, itemContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp), content = {
                        Text(
                            text = it.dateStr + " " + it.action.toString() + " " + it.data.toString(),
                            fontSize = 15.sp,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                )
            })
        })
    })
}