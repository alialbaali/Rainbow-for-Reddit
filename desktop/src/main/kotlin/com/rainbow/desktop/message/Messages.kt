package com.rainbow.desktop.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Message

fun LazyListScope.messages(
    messagesState: UIState<List<Message>>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onLoadMore: (Message) -> Unit,
) {
    val messages = messagesState.getOrDefault(emptyList())
    itemsIndexed(messages) { index, message ->
        MessageItem(
            message,
            onNavigateMainScreen,
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    when (val type = message.type) {
                        is Message.Type.Message -> onNavigateDetailsScreen(DetailsScreen.Message(message.id))
                        is Message.Type.Mention -> onNavigateDetailsScreen(DetailsScreen.Post(type.postId))
                        is Message.Type.CommentReply -> onNavigateDetailsScreen(DetailsScreen.Post(type.postId))
                        is Message.Type.PostReply -> onNavigateDetailsScreen(DetailsScreen.Post(type.postId))
                    }
                }
        )
        PagingEffect(index, messages.lastIndex) {
            onLoadMore(message)
        }
    }

    if (messagesState.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}