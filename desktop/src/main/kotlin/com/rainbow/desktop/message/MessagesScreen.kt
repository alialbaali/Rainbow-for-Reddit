package com.rainbow.desktop.message

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.message.MessageTab
import com.rainbow.desktop.message.MessagesScreenModel
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.utils.OneTimeEffect
import com.rainbow.domain.models.Message

@Composable
inline fun MessagesScreen(
    crossinline onMessageClick: (Message) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
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
    OneTimeEffect(
        selectedTab,
        inboxMessages.isLoading,
        unreadMessages.isLoading,
        sentMessages.isLoading,
        messages.isLoading,
        mentions.isLoading,
        postMessages.isLoading,
        commentMessages.isLoading
    ) {
        when (selectedTab) {
            MessageTab.Inbox -> setListModel(MessagesScreenModel.inboxMessages)
            MessageTab.Unread -> setListModel(MessagesScreenModel.unreadMessages)
            MessageTab.Sent -> setListModel(MessagesScreenModel.sentMessages)
            MessageTab.Messages -> setListModel(MessagesScreenModel.messages)
            MessageTab.Mentions -> setListModel(MessagesScreenModel.mentions)
            MessageTab.PostMessages -> setListModel(MessagesScreenModel.postMessages)
            MessageTab.CommentMessages -> setListModel(MessagesScreenModel.commentMessages)
        }
    }
    LazyColumn(modifier) {
        item {
            ScrollableEnumTabRow(
                selectedTab = selectedTab,
                onTabClick = { MessagesScreenModel.selectTab(it) },
                Modifier.fillParentMaxWidth()
            )
        }
        when (selectedTab) {
            MessageTab.Inbox -> messages(inboxMessages, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.Unread -> messages(unreadMessages, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.Sent -> messages(sentMessages, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.Messages -> messages(messages, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.Mentions -> messages(mentions, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.PostMessages -> messages(postMessages, onMessageClick, onUserNameClick, onSubredditNameClick)
            MessageTab.CommentMessages -> messages(commentMessages,
                onMessageClick,
                onUserNameClick,
                onSubredditNameClick)
        }
    }
}