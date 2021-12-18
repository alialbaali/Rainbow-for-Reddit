package com.rainbow.app.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MessageScreen(model: MessageScreenModel, modifier: Modifier = Modifier) {
    val message by model.message.collectAsState()
    var text by remember { mutableStateOf("") }
    Column(modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        MessageItem(message, onClick = {}, onUserNameClick = {})
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}