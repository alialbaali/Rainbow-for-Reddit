package com.rainbow.app.comment

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowIcons

@Composable
fun AddComment(modifier: Modifier = Modifier) {

    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = MaterialTheme.colors.background),
        trailingIcon = {
            Icon(RainbowIcons.Send, contentDescription = "Comment")
        }
    )

}