package com.rainbow.desktop.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.DropdownMenuHolder
import com.rainbow.desktop.components.EnumTabRow
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.desktop.utils.getOrNull

@Composable
inline fun HomeScreen(
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { HomeScreenStateHolder() }
    val posts by stateHolder.postsStateHolder.items.collectAsState()
    val postSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val timeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val comments by stateHolder.commentsStateHolder.items.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    DisposableEffect(posts.isLoading) {
        val post = posts.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateContentScreen(ContentScreen.Post(post.id))
        }
        onDispose {
            onNavigateContentScreen(ContentScreen.None)
        }
    }
    DisposableEffect(comments.isLoading) {
        val comment = comments.getOrNull()?.firstOrNull()
        if (comment != null) {
            onNavigateContentScreen(ContentScreen.Post(comment.postId))
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
                        onValueUpdate = stateHolder.postsStateHolder::setSorting,
                    )

                    DropdownMenuHolder(
                        value = timeSorting,
                        onValueUpdate = stateHolder.postsStateHolder::setTimeSorting,
                        enabled = postSorting.isTimeSorting,
                    )
                }
            }
        }

        when (selectedTab) {
            HomeTab.Posts -> {
                posts(
                    posts,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.postsStateHolder::setLastItem,
                )
            }

            HomeTab.Comments -> {
                comments(
                    comments,
                    onNavigate,
                    onNavigateContentScreen,
                    stateHolder.commentsStateHolder::setLastItem,
                )
            }
        }
    }
}