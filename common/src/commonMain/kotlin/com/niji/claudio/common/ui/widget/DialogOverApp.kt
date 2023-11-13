package com.niji.claudio.common.ui.widget

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable


@Composable
fun DialogOverApp(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        ClaudioAlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK") }
            },
            title = { Text(text = "Allow over app permission") },
            text = { Text(text = "To use app correctly, allow over app permission.") }
        )
    }
}