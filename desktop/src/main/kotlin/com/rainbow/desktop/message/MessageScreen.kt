package com.rainbow.desktop.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.DefaultContentPadding
import com.rainbow.desktop.utils.getOrNull

@Composable
fun MessageScreen(
    messageId: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(messageId) { MessageScreenStateHolder.getInstance(messageId) }
    val messageState by stateHolder.message.collectAsState()
    val message = remember(messageState) { messageState.getOrNull() }

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(DefaultContentPadding()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (message != null) {
            SelectionContainer {
                MessageItem(message, onNavigateMainScreen)
            }
        }
    }
}