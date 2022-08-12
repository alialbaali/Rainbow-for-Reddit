package com.rainbow.desktop.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Message

inline fun LazyListScope.messages(
    messagesState: UIState<List<Message>>,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
) {
    when (messagesState) {
        is UIState.Failure -> item { Text("Failed loading messages") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(messagesState.data) { message ->
            MessageItem(
                message,
                onNavigate,
                Modifier.clickable {
                    onNavigateContentScreen(ContentScreen.Message(message))
                }
            )
        }

        is UIState.Empty -> {}
    }
}