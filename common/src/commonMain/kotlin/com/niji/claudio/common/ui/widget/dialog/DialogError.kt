package com.niji.claudio.common.ui.widget.dialog

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.niji.claudio.common.ui.widget.base.ClaudioAlertDialog

@Composable
fun DialogError(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        ClaudioAlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            confirmButton = {
                TextButton(onClick = { onDismiss.invoke() })
                { Text(text = "OK") }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "An error occurred :(") }
        )
    }
}