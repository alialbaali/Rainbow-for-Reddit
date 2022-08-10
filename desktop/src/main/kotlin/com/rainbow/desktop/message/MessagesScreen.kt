package com.rainbow.desktop.message

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen

@Composable
inline fun MessagesScreen(
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTab by MessagesScreenStateHolder.selectedTab.collectAsState()
    val inboxMessages by MessagesScreenStateHolder.inboxMessages.items.collectAsState()
    val unreadMessages by MessagesScreenStateHolder.unreadMessages.items.collectAsState()
    val sentMessages by MessagesScreenStateHolder.sentMessages.items.collectAsState()
    val messages by MessagesScreenStateHolder.messages.items.collectAsState()
    val mentions by MessagesScreenStateHolder.mentions.items.collectAsState()
    val postMessages by MessagesScreenStateHolder.postMessages.items.collectAsState()
    val commentMessages by MessagesScreenStateHolder.commentMessages.items.collectAsState()
    LazyColumn(modifier) {
        item {
            ScrollableEnumTabRow(
                selectedTab = selectedTab,
                onTabClick = { MessagesScreenStateHolder.selectTab(it) },
                Modifier.fillParentMaxWidth()
            )
        }
        when (selectedTab) {
            MessageTab.Inbox -> messages(
                inboxMessages,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.Unread -> messages(
                unreadMessages,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.Sent -> messages(
                sentMessages,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.Messages -> messages(
                messages,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.Mentions -> messages(
                mentions,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.PostMessages -> messages(
                postMessages,
                onNavigate,
                onNavigateContentScreen,
            )

            MessageTab.CommentMessages -> messages(
                commentMessages,
                onNavigate,
                onNavigateContentScreen,
            )
        }
    }
}