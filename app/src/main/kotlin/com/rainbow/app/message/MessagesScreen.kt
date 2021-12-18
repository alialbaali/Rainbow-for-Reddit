package com.rainbow.app.message

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.model.ListModel
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
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTab by MessagesScreenModel.selectedTab.collectAsState()
    val inboxMessages by MessagesScreenModel.inboxMessages.items.collectAsState()
    val unreadMessages by MessagesScreenModel.unreadMessages.items.collectAsState()
    val sentMessages by MessagesScreenModel.sentMessages.items.collectAsState()
    val messages by MessagesScreenModel.messages.items.collectAsState()
    val mentions by MessagesScreenModel.mentions.items.collectAsState()
    val postMessages by MessagesScreenModel.postMessages.items.collectAsState()
    val commentMessages by MessagesScreenModel.commentMessages.items.collectAsState()
    RainbowLazyColumn(modifier) {
        item {
            DefaultTabRow(
                selectedTab = selectedTab,
                onTabClick = { MessagesScreenModel.selectTab(it) },
                Modifier.fillParentMaxWidth()
            )
        }
        when (selectedTab) {
            MessageTab.Inbox -> {
                setListModel(MessagesScreenModel.inboxMessages)
                messages(inboxMessages, onMessageClick, onUserNameClick)
            }
            MessageTab.Unread -> {
                setListModel(MessagesScreenModel.unreadMessages)
                messages(unreadMessages, onMessageClick, onUserNameClick)
            }
            MessageTab.Sent -> {
                setListModel(MessagesScreenModel.sentMessages)
                messages(sentMessages, onMessageClick, onUserNameClick)
            }
            MessageTab.Messages -> {
                setListModel(MessagesScreenModel.messages)
                messages(messages, onMessageClick, onUserNameClick)
            }
            MessageTab.Mentions -> {
                setListModel(MessagesScreenModel.mentions)
                messages(mentions, onMessageClick, onUserNameClick)
            }
            MessageTab.PostMessages -> {
                setListModel(MessagesScreenModel.postMessages)
                messages(postMessages, onMessageClick, onUserNameClick)
            }
            MessageTab.CommentMessages -> {
                setListModel(MessagesScreenModel.commentMessages)
                messages(commentMessages, onMessageClick, onUserNameClick)
            }
        }
    }
}