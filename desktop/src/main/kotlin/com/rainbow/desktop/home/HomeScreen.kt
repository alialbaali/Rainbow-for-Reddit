package com.rainbow.desktop.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.EnumTabRow
import com.rainbow.desktop.components.PostSorting
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.Comments
import com.rainbow.desktop.utils.Posts
import com.rainbow.desktop.utils.RainbowIcons

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { HomeScreenStateHolder.Instance }
    val posts by stateHolder.postsStateHolder.items.collectAsState()
    val postSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val timeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val comments by stateHolder.commentsStateHolder.items.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val selectedItemIds by stateHolder.selectedItemIds.collectAsState()
    val postLayout by stateHolder.postLayout.collectAsState()

    RainbowLazyColumn(modifier) {
        stickyHeader {
            EnumTabRow(
                selectedTab,
                onTabClick = { stateHolder.selectTab(it) },
                icon = { Icon(it.icon, it.name) }
            )
        }

        when (selectedTab) {
            HomeTab.Posts -> {
                item {
                    PostSorting(
                        postSorting,
                        timeSorting,
                        stateHolder.postsStateHolder::setSorting,
                        stateHolder.postsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    posts,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(HomeTab.Posts, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.postsStateHolder::setLastItem,
                )
            }

            HomeTab.Comments -> {
                comments(
                    comments,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(HomeTab.Comments, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.commentsStateHolder::setLastItem,
                )
            }
        }
    }

    DisposableEffect(selectedTab, selectedItemIds) {
        selectedItemIds[selectedTab]?.let { postId ->
            onNavigateDetailsScreen(DetailsScreen.Post(postId))
        }
        onDispose {}
    }
}

private val HomeTab.icon
    get() = when (this) {
        HomeTab.Posts -> RainbowIcons.Posts
        HomeTab.Comments -> RainbowIcons.Comments
    }