package com.rainbow.desktop.user

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.item.items
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.components.PostSorting
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.profile.Header
import com.rainbow.desktop.utils.fold

@Composable
fun UserScreen(
    userName: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(userName) { UserScreenStateHolder.getInstance(userName) }
    val userState by stateHolder.user.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val items by stateHolder.itemsStateHolder.items.collectAsState()
    val itemsSorting by stateHolder.itemsStateHolder.sorting.collectAsState()
    val itemsTimeSorting by stateHolder.itemsStateHolder.timeSorting.collectAsState()
    val posts by stateHolder.postsStateHolder.items.collectAsState()
    val postsSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val postsTimeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val comments by stateHolder.commentsStateHolder.items.collectAsState()
    val commentsSorting by stateHolder.commentsStateHolder.sorting.collectAsState()
    val commentsTimeSorting by stateHolder.commentsStateHolder.timeSorting.collectAsState()
    val selectedItemIds by stateHolder.selectedItemIds.collectAsState()

    RainbowLazyColumn(modifier) {
        userState.fold(
            onLoading = {
                item {
                    RainbowProgressIndicator()
                }
            },
            onSuccess = { user ->
                item { Header(user) }
                item {
                    ScrollableEnumTabRow(
                        selectedTab = selectedTab,
                        onTabClick = { stateHolder.selectTab(it) },
                    )
                }
            },
            onFailure = { value, exception ->

            },
            onEmpty = {},
        )

        when (selectedTab) {
            UserTab.Overview -> {
                item {
                    PostSorting(
                        itemsSorting,
                        itemsTimeSorting,
                        stateHolder.itemsStateHolder::setSorting,
                        stateHolder.itemsStateHolder::setTimeSorting,
                    )
                }

                items(
                    items,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(UserTab.Overview, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.itemsStateHolder::setLastItem,
                )
            }

            UserTab.Submitted -> {
                item {
                    PostSorting(
                        postsSorting,
                        postsTimeSorting,
                        stateHolder.postsStateHolder::setSorting,
                        stateHolder.postsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    posts,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(UserTab.Submitted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.postsStateHolder::setLastItem,
                )
            }

            UserTab.Comments -> {
                item {
                    PostSorting(
                        commentsSorting,
                        commentsTimeSorting,
                        stateHolder.commentsStateHolder::setSorting,
                        stateHolder.commentsStateHolder::setTimeSorting,
                    )
                }

                comments(
                    comments,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(UserTab.Comments, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.commentsStateHolder::setLastItem,
                )
            }
        }

        if (userState.isLoading) {
            item { RainbowProgressIndicator() }
        }
    }

    DisposableEffect(selectedTab, selectedItemIds) {
        selectedItemIds[selectedTab]?.let { postId ->
            onNavigateDetailsScreen(DetailsScreen.Post(postId))
        }
        onDispose {}
    }
}