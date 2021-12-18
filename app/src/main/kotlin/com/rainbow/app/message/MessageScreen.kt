package com.rainbow.app.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
inline fun MessageScreen(
    model: MessageScreenModel,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val message by model.message.collectAsState()
    var text by remember { mutableStateOf("") }
    Column(modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        MessageItem(message, onUserNameClick, onSubredditNameClick)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}