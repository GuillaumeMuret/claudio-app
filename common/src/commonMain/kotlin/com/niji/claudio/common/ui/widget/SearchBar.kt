package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.ui.MediasViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(mVm: MediasViewModel) {
    val query = mVm.query.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = query.value,
                onValueChange = { mVm.onQueryChanged(it) },
                label = { Text(text = "Search") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(0.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        mVm.onExecuteSearch()
                        keyboardController?.hide()
                    },
                ),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean = false,
    onSelectedCategoryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedCategoryChanged(category)
                    onExecuteSearch()
                }
            )
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
