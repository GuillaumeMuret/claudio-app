package com.niji.claudio.common.ui.widget

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable

@Composable
fun ClaudioAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    title: @Composable (() -> Unit)?,
    text: @Composable (() -> Unit)?
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        title = title,
        text = text,
    )
}
