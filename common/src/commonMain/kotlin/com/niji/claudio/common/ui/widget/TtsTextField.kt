package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.theme.green
import com.niji.claudio.common.ui.theme.teal

@Composable
fun TtsTextField(mVm: MediasViewModel) {
    var text by remember { mutableStateOf(mVm.ttsText) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            TextField(
                modifier = Modifier.width(200.dp),
                value = text,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.onBackground
                ),
                onValueChange = {
                    text = it
                    mVm.ttsText = it
                },
                label = { Text("Text To Speech") },
                singleLine = true
            )
            Row(Modifier.wrapContentHeight()) {
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_CLEAR),
                    color = MaterialTheme.colors.teal,
                    contentDescription = "Clear text",
                    action = {
                        mVm.ttsText = ""
                        text = ""
                    },
                    mustReload = {}
                )
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_PLAY),
                    color = MaterialTheme.colors.green,
                    contentDescription = "launch tts",
                    action = mVm::launchTts,
                    mustReload = {}
                )
                Image(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_VIBRATE),
                    contentDescription = "Micro",
                    modifier = Modifier.clickable { mVm.vibrate() }
                        .size(50.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}