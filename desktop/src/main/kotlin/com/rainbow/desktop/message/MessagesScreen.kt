package com.rainbow.desktop.message

import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.Comments
import com.rainbow.desktop.utils.Posts
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.User

@Composable
fun MessagesScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { MessagesScreenStateHolder.Instance }
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val inboxMessages by stateHolder.inboxMessages.items.collectAsState()
    val unreadMessages by stateHolder.unreadMessages.items.collectAsState()
    val sentMessages by stateHolder.sentMessages.items.collectAsState()
    val messages by stateHolder.messages.items.collectAsState()
    val mentions by stateHolder.mentions.items.collectAsState()
    val postMessages by stateHolder.postMessages.items.collectAsState()
    val commentMessages by stateHolder.commentMessages.items.collectAsState()
    val selectedItemIds by stateHolder.selectedItemIds.collectAsState()

    RainbowLazyColumn(modifier) {
        item {
            ScrollableEnumTabRow(
                selectedTab = selectedTab,
                onTabClick = stateHolder::selectTab,
                Modifier.fillParentMaxWidth(),
                icon = { Icon(it.icon, it.name) },
            )
        }
        when (selectedTab) {
            MessageTab.Inbox -> messages(
                inboxMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Inbox, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.Inbox, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.inboxMessages::setLastItem,
            )

            MessageTab.Unread -> messages(
                unreadMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Unread, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.Unread, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.unreadMessages::setLastItem,
            )

            MessageTab.Sent -> messages(
                sentMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Sent, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.Sent, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.sentMessages::setLastItem,
            )

            MessageTab.Messages -> messages(
                messages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Messages, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.Messages, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.messages::setLastItem,
            )

            MessageTab.Mentions -> messages(
                mentions,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Mentions, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.Mentions, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.mentions::setLastItem,
            )

            MessageTab.PostMessages -> messages(
                postMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.PostMessages, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.PostMessages, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.postMessages::setLastItem,
            )

            MessageTab.CommentMessages -> messages(
                commentMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.CommentMessages, detailsScreen.messageId)
                    } else if (detailsScreen is DetailsScreen.Post) {
                        stateHolder.selectItemId(MessageTab.CommentMessages, detailsScreen.postId)
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.commentMessages::setLastItem,
            )
        }
    }

    DisposableEffect(selectedTab, selectedItemIds) {
        selectedItemIds[selectedTab]?.let { itemId ->
            val detailsScreen = if (itemId.startsWith("t3")) {
                DetailsScreen.Post(itemId)
            } else {
                DetailsScreen.Message(itemId)
            }
            onNavigateDetailsScreen(detailsScreen)
        }
        onDispose {}
    }
}

private val MessageTab.icon
    get() = when (this) {
        MessageTab.Inbox -> RainbowIcons.Inbox
        MessageTab.Unread -> RainbowIcons.MarkEmailUnread
        MessageTab.Sent -> RainbowIcons.Send
        MessageTab.Messages -> RainbowIcons.Email
        MessageTab.Mentions -> RainbowIcons.User
        MessageTab.PostMessages -> RainbowIcons.Posts
        MessageTab.CommentMessages -> RainbowIcons.Comments
    }