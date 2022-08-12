package com.rainbow.desktop.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.PostItem
import com.rainbow.desktop.profile.Header
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.composed

@Composable
fun UserScreen(
    userName: String,
    onNavigate: (Screen) -> Unit,
    onNavigateContentScreen: (ContentScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(userName) { UserScreenStateHolder.getOrCreateInstance(userName) }
    val userState by stateHolder.user.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
//    val postLayout by model.itemListModel.postLayout.collectAsState()
//    val itemsState by model.itemListModel.items.collectAsState()
    val posts by stateHolder.postsStateHolder.items.collectAsState()
    val postsState by stateHolder.postsStateHolder.itemsState.collectAsState()
//    val commentsState by model.commentListModel.items.collectAsState()
    userState.composed(onShowSnackbar) { user ->
        LazyColumn(modifier) {
            item { Header(user) }
            item {
                ScrollableEnumTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { stateHolder.selectTab(it) }
                )
            }
            when (selectedTab) {
//                UserTab.Overview -> items(
//                    itemsState,
//                    onNavigate,
//                    onNavigateContentScreen,
//                    { },
//                    onShowSnackbar,
//                )

                UserTab.Submitted -> {
                    itemsIndexed(posts) { index, post ->
                        PostItem(
                            post,
                            onNavigate,
                            onNavigateContentScreen,
                            {},
                            onShowSnackbar,
                            Modifier.fillParentMaxWidth(),
                        )
                        if (postsState.isSuccess) {
                            PagingEffect(posts, index, stateHolder.postsStateHolder::setLastItem)
                        }
                    }
                }

//                UserTab.Comments -> comments(
//                    commentsState,
//                    onNavigate,
//                    onNavigateContentScreen,
//                )
                else -> {}
            }
            if (postsState.isLoading) {
                item {
                    RainbowProgressIndicator()
                }
            }
        }
    }
}