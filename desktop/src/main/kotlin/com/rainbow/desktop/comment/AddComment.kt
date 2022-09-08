package com.rainbow.desktop.comment

import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowIconButton
import com.rainbow.desktop.components.RainbowTextField
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Post

@Composable
fun AddComment(post: Post, modifier: Modifier = Modifier) {
    var text by remember(post) { mutableStateOf("") }

    RainbowTextField(
        value = text,
        onValueChange = { text = it },
        placeholderText = RainbowStrings.AddComment,
        modifier,
        trailingIcon = { RainbowIconButton(RainbowIcons.Send, RainbowStrings.AddComment, onClick = {}) },
        singleLine = false,
        maxLines = 3,
    )
}