package com.rainbow.desktop.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Message

inline fun LazyListScope.messages(
    messagesState: UIState<List<Message>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    crossinline onLoadMore: (Message) -> Unit,
) {
    val messages = messagesState.getOrDefault(emptyList())
    itemsIndexed(messages) { index, message ->
        MessageItem(
            message,
            onNavigateMainScreen,
            Modifier.clickable {
                onNavigateDetailsScreen(DetailsScreen.Message(message))
            }
        )
        PagingEffect(messages, index, onLoadMore)
    }

    if (messagesState.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}