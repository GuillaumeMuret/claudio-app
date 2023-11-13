package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.ui.MediasViewModel


@Composable
fun DialogUserName(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    mVm: MediasViewModel
) {
    if (show) {
        var text by remember { mutableStateOf("") }
        ClaudioAlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK") }
            },
            title = { Text(text = "Who are you ?") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(text = "Please give your name :)")
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            mVm.currentDialogDeviceName = it
                        },
                        singleLine = true
                    )
                }
            },
        )
    }
}