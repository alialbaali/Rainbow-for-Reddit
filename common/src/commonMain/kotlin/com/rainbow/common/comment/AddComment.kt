package com.rainbow.common.comment

import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.common.components.RainbowIconButton
import com.rainbow.common.components.RainbowTextField
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
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