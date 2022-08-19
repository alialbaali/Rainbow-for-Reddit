package com.rainbow.desktop.message

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen

@Composable
inline fun MessagesScreen(
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTab by MessagesScreenStateHolder.selectedTab.collectAsState()
//    val inboxMessages by MessagesScreenStateHolder.inboxMessages.items.collectAsState()
//    val unreadMessages by MessagesScreenStateHolder.unreadMessages.items.collectAsState()
//    val sentMessages by MessagesScreenStateHolder.sentMessages.items.collectAsState()
//    val messages by MessagesScreenStateHolder.messages.items.collectAsState()
//    val mentions by MessagesScreenStateHolder.mentions.items.collectAsState()
//    val postMessages by MessagesScreenStateHolder.postMessages.items.collectAsState()
//    val commentMessages by MessagesScreenStateHolder.commentMessages.items.collectAsState()
    LazyColumn(modifier) {
        item {
            ScrollableEnumTabRow(
                selectedTab = selectedTab,
                onTabClick = { MessagesScreenStateHolder.selectTab(it) },
                Modifier.fillParentMaxWidth()
            )
        }
//        when (selectedTab) {
//            MessageTab.Inbox -> messages(
//                inboxMessages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.Unread -> messages(
//                unreadMessages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.Sent -> messages(
//                sentMessages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.Messages -> messages(
//                messages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.Mentions -> messages(
//                mentions,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.PostMessages -> messages(
//                postMessages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//
//            MessageTab.CommentMessages -> messages(
//                commentMessages,
//                onNavigateMainScreen,
//                onNavigateDetailsScreen,
//            )
//        }
    }
}