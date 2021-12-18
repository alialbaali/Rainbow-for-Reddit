package com.rainbow.app.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Message

inline fun LazyListScope.messages(
    messagesState: UIState<List<Message>>,
    crossinline onMessageClick: (Message) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
) {
    when (messagesState) {
        is UIState.Empty -> item { Text("No messages found.") }
        is UIState.Failure -> item { Text("Failed loading messages") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(messagesState.value) { message ->
            MessageItem(message, onUserNameClick, onSubredditNameClick, Modifier.clickable { onMessageClick(message) })
        }
    }
}