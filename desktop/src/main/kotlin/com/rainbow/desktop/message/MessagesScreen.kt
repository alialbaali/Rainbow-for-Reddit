package com.rainbow.desktop.message

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen

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
                Modifier.fillParentMaxWidth()
            )
        }
        when (selectedTab) {
            MessageTab.Inbox -> messages(
                inboxMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen = { detailsScreen ->
                    if (detailsScreen is DetailsScreen.Message) {
                        stateHolder.selectItemId(MessageTab.Inbox, detailsScreen.messageId)
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
                    }
                    onNavigateDetailsScreen(detailsScreen)
                },
                stateHolder.commentMessages::setLastItem,
            )
        }
    }

    DisposableEffect(selectedTab, selectedItemIds) {
        selectedItemIds[selectedTab]?.let { messageId ->
            onNavigateDetailsScreen(DetailsScreen.Message(messageId))
        }
        onDispose {}
    }
}