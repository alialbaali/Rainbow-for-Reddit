package com.rainbow.desktop.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.DropdownMenuHolder
import com.rainbow.desktop.components.EnumTabRow
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.PostItem
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.desktop.utils.PagingEffect

@Composable
inline fun HomeScreen(
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { HomeScreenStateHolder() }
    val state by stateHolder.state.collectAsState()
    val posts by stateHolder.posts.collectAsState()
    val postSorting by stateHolder.postSorting.collectAsState()
    val timeSorting by stateHolder.timeSorting.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    DisposableEffect(posts.isEmpty()) {
        val post = posts.firstOrNull()
        if (post != null) {
            onNavigateContentScreen(ContentScreen.PostEntity(post.id))
        }
        onDispose {
            onNavigateContentScreen(ContentScreen.None)
        }
    }
    RainbowLazyColumn(modifier) {

        item {
            EnumTabRow(
                selectedTab,
                onTabClick = { stateHolder.selectTab(it) }
            )
        }

        if (selectedTab == HomeTab.Posts) {
            item {
                Row(
                    Modifier.fillParentMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DropdownMenuHolder(
                        value = postSorting,
                        onValueUpdate = { stateHolder.setPostSorting(it) },
                    )

                    DropdownMenuHolder(
                        value = timeSorting,
                        onValueUpdate = { stateHolder.setTimeSorting(it) },
                        enabled = postSorting.isTimeSorting,
                    )
                }
            }
        }

        when (selectedTab) {
            HomeTab.Posts -> {
                itemsIndexed(posts) { index, post ->
                    PostItem(
                        post,
                        onNavigate,
                        onNavigateContentScreen,
                        {},
                        onShowSnackbar,
                        Modifier.fillParentMaxWidth(),
                    )
                    if (state.isSuccess) {
                        PagingEffect(posts, index) {
                            stateHolder.setLastPostId(it.id)
                        }
                    }
                }
            }

            HomeTab.Comments -> {
//                comments(
//                    commentsState,
//                    onUserNameClick,
//                    onSubredditNameClick,
//                    onCommentClick,
//                    onCommentUpdate,
//                )
            }
        }

        if (state.isLoading) {
            item {
                RainbowProgressIndicator()
            }
        }
    }
}
