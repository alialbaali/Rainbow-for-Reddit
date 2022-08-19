package com.rainbow.desktop.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.navigation.MainScreen

@Composable
inline fun MessageScreen(
    model: MessageScreenModel,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val message by model.message.collectAsState()
    var text by remember { mutableStateOf("") }
    Column(modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        MessageItem(message, onNavigateMainScreen)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}