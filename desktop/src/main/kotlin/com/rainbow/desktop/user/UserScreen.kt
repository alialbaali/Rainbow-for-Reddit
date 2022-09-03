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
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.profile.Header
import com.rainbow.desktop.utils.fold
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.desktop.utils.getOrNull

@Composable
fun UserScreen(
    userName: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(userName) { UserScreenStateHolder.getOrCreateInstance(userName) }
    val userState by stateHolder.user.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val items by stateHolder.itemsStateHolder.items.collectAsState()
    val posts by stateHolder.postsStateHolder.items.collectAsState()
    val comments by stateHolder.commentsStateHolder.items.collectAsState()

    DisposableEffect(items.getOrDefault(emptyList()).isEmpty()) {
        val item = items.getOrNull()?.firstOrNull()
        if (item != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(item.postId))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(posts.getOrDefault(emptyList()).isEmpty()) {
        val post = posts.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(comments.getOrDefault(emptyList()).isEmpty()) {
        val comment = comments.getOrNull()?.firstOrNull()
        if (comment != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(comment.postId))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

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
            UserTab.Overview -> items(
                items,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onAwardsClick = {},
                onShowSnackbar,
                stateHolder.itemsStateHolder::setLastItem,
            )

            UserTab.Submitted -> posts(
                posts,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onAwardsClick = {},
                onShowSnackbar,
                stateHolder.postsStateHolder::setLastItem,
            )

            UserTab.Comments -> comments(
                comments,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                stateHolder.commentsStateHolder::setLastItem,
            )
        }

        if (userState.isLoading) {
            item { RainbowProgressIndicator() }
        }
    }
}