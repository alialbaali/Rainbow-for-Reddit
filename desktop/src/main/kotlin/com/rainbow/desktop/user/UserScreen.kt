package com.rainbow.desktop.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.item.items
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.profile.Header
import com.rainbow.desktop.utils.composed

@Composable
fun UserScreen(
    userName: String,
    onNavigate: (Screen) -> Unit,
    onNavigateContentScreen: (ContentScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember(userName) { UserScreenStateHolder.getOrCreateInstance(userName) }
    val userState by model.user.collectAsState()
    val selectedTab by model.selectedTab.collectAsState()
    val postLayout by model.itemListModel.postLayout.collectAsState()
    val itemsState by model.itemListModel.items.collectAsState()
    val postsState by model.postListModel.items.collectAsState()
    val commentsState by model.commentListModel.items.collectAsState()
    userState.composed(onShowSnackbar) { user ->
        LazyColumn(modifier) {
            item { Header(user) }
            item {
                ScrollableEnumTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { model.selectTab(it) }
                )
            }
            when (selectedTab) {
                UserTab.Overview -> items(
                    itemsState,
                    onNavigate,
                    onNavigateContentScreen,
                    { },
                    onShowSnackbar,
                )

                UserTab.Submitted -> posts(
                    postsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    onShowSnackbar,
                )

                UserTab.Comments -> comments(
                    commentsState,
                    onNavigate,
                    onNavigateContentScreen,
                )
            }
        }
    }
}