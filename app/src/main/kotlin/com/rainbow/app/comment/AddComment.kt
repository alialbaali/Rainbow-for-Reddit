package com.rainbow.app.comment

import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.domain.models.Post
import kotlinx.coroutines.launch

@Composable
fun AddComment(post: Post, modifier: Modifier = Modifier) {

    var text by remember(post) { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = MaterialTheme.colors.background),
        trailingIcon = {
            IconButton(onClick = {
                scope.launch {
//                    Repos.Comment.createComment()
                }
            }) {
                Icon(RainbowIcons.Send, contentDescription = "Comment")
            }
        },
    )
}