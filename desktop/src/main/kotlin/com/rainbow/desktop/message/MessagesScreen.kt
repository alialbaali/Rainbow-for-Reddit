package com.rainbow.desktop.message

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    val stateHolder = remember { MessagesScreenStateHolder.getInstance() }
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val inboxMessages by stateHolder.inboxMessages.items.collectAsState()
    val unreadMessages by stateHolder.unreadMessages.items.collectAsState()
    val sentMessages by stateHolder.sentMessages.items.collectAsState()
    val messages by stateHolder.messages.items.collectAsState()
    val mentions by stateHolder.mentions.items.collectAsState()
    val postMessages by stateHolder.postMessages.items.collectAsState()
    val commentMessages by stateHolder.commentMessages.items.collectAsState()
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
                onNavigateDetailsScreen,
                stateHolder.inboxMessages::setLastItem,
            )

            MessageTab.Unread -> messages(
                unreadMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.unreadMessages::setLastItem,
            )

            MessageTab.Sent -> messages(
                sentMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.sentMessages::setLastItem,
            )

            MessageTab.Messages -> messages(
                messages,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.messages::setLastItem,
            )

            MessageTab.Mentions -> messages(
                mentions,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.mentions::setLastItem,
            )

            MessageTab.PostMessages -> messages(
                postMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.postMessages::setLastItem,
            )

            MessageTab.CommentMessages -> messages(
                commentMessages,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.commentMessages::setLastItem,
            )
        }
    }
}