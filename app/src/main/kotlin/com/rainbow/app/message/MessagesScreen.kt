package com.rainbow.app.message

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Message

enum class MessageTab {
    Inbox, Unread, Sent, Messages, Mentions, PostMessages, CommentMessages;

    companion object {
        val Default = Inbox
    }
}

@Composable
inline fun MessagesScreen(
    crossinline onMessageClick: (Message) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollingState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(MessageTab.Default) }
    val state by produceState<UIState<List<Message>>>(UIState.Loading, selectedTab) {
        value = when (selectedTab) {
            MessageTab.Inbox -> Repos.Message.getInboxMessages()
            MessageTab.Unread -> Repos.Message.getUnreadMessages()
            MessageTab.Sent -> Repos.Message.getSentMessages()
            MessageTab.Messages -> Repos.Message.getMessages()
            MessageTab.Mentions -> Repos.Message.getMentions()
            MessageTab.PostMessages -> Repos.Message.getPostMessages()
            MessageTab.CommentMessages -> Repos.Message.getCommentMessages()
        }.toUIState()
    }
    state.composed(onShowSnackbar, modifier) { messages ->
        OneTimeEffect(messages) {
            messages.firstOrNull()?.let { onMessageClick(it) }
        }

        LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it },
                    Modifier.fillParentMaxWidth()
                )
            }
            items(messages) { message ->
                MessageItem(message, onMessageClick, onUserNameClick)
            }
        }

        VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
    }
}