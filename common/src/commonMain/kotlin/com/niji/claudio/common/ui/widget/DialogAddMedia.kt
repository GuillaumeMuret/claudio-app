package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.tool.MediaUtils
import com.niji.claudio.common.tool.UiUtils
import com.niji.claudio.common.ui.MediasViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DialogAddMedia(
    mVm: MediasViewModel,
    window: Any? = null,
    launchFileChooserIntent: (() -> Unit)? = null,
) {
    val showDialogAddMediaState: DialogAddMediaViewState by mVm.dialogAddMediaState.collectAsState()
    val androidFilePath by mVm.addMediaFilePath.collectAsState()
    if (showDialogAddMediaState !is DialogAddMediaViewState.Hide) {
        var title by rememberSaveable { mutableStateOf("") }
        var category by rememberSaveable { mutableStateOf("") }
        var filePath by rememberSaveable { mutableStateOf("") }
        var filename by rememberSaveable { mutableStateOf("") }
        if (androidFilePath.isNotEmpty()) {
            filename = mVm.addMediaFilename
            filePath = androidFilePath
        }
        var isErrorTitle: Boolean by mutableStateOf(false)
        var isErrorCategory: Boolean by mutableStateOf(false)
        if (showDialogAddMediaState is DialogAddMediaViewState.Error) {
            isErrorTitle = (showDialogAddMediaState as DialogAddMediaViewState.Error).isErrorTitle
            isErrorCategory =
                (showDialogAddMediaState as DialogAddMediaViewState.Error).isErrorCategory
        }
        ClaudioAlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(text = "Add Media") },
            text = {
                Column(
                    Modifier.widthIn(0.dp, 500.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.width(100.dp), text = "Title")
                        TextField(
                            modifier = Modifier.weight(1F),
                            value = title,
                            onValueChange = {
                                title = it
                            },
                            isError = isErrorTitle,
                            singleLine = true
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.width(100.dp), text = "Category")
                        TextField(
                            modifier = Modifier.weight(1F),
                            value = category,
                            onValueChange = {
                                category = it
                            },
                            isError = isErrorCategory,
                            singleLine = true
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier.width(100.dp), text = "File")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = {
                                window?.let {
                                    MediaUtils.fileChooser(
                                        window = it,
                                        launchFileChooserIntent = launchFileChooserIntent
                                    )
                                }?.let {
                                    filePath = it.filePath ?: ""
                                    filename = it.filename ?: ""
                                }
                            }) {
                                Text("Browse")
                            }
                            Text(modifier = Modifier.padding(start = 10.dp), text = filename)
                        }
                    }
                }
            },
            confirmButton = {
                if (showDialogAddMediaState is DialogAddMediaViewState.Loading) {
                    ProgressIndicator(MutableStateFlow(true))
                } else {
                    Row {
                        TextButton(onClick = {
                            mVm.onAddMediaDialogCancel()
                        }) { Text(text = "Cancel") }
                        TextButton(onClick = {
                            isErrorTitle = !UiUtils.isCorrectAddMediaField(title)
                            isErrorCategory = !UiUtils.isCorrectAddMediaField(category)
                            if (!isErrorTitle && !isErrorCategory && filePath.isNotEmpty()) {
                                mVm.onAddMediaDialogOk(title, category, filePath, filename)
                            }
                        }) { Text(text = "Go") }
                    }
                }
            },
        )
    }
}

sealed class DialogAddMediaViewState {
    object Show : DialogAddMediaViewState()
    class Error(
        val isErrorTitle: Boolean = false,
        val isErrorCategory: Boolean = false,
        val isErrorFilePath: Boolean = false
    ) : DialogAddMediaViewState()

    object Loading : DialogAddMediaViewState()
    object Hide : DialogAddMediaViewState()
}
