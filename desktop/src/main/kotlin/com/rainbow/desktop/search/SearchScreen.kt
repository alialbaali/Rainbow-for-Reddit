package com.rainbow.desktop.search

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.PostSorting
import com.rainbow.desktop.components.RainbowLazyVerticalGrid
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.subreddit.searchSubreddits
import com.rainbow.desktop.user.users
import com.rainbow.desktop.utils.Posts
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.User
import com.rainbow.desktop.utils.getOrDefault

@Composable
fun SearchScreen(
    searchTerm: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(searchTerm) { SearchScreenStateHolder.getInstance(searchTerm) }
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val subredditsState by stateHolder.subredditsStateHolder.items.collectAsState()
    val postsState by stateHolder.postsStateHolder.items.collectAsState()
    val postsSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val postsTimeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val usersState by stateHolder.usersStateHolder.items.collectAsState()
    val postLayout by stateHolder.postLayout.collectAsState()
    val selectedItemId by stateHolder.selectedItemId.collectAsState()
    val appliedModifier = remember(selectedTab) { if (selectedTab == SearchTab.Posts) modifier else Modifier }
    val columnsCount = remember(selectedTab) {
        when (selectedTab) {
            SearchTab.Subreddits -> 4
            SearchTab.Posts -> 1
            SearchTab.Users -> 4
        }
    }
    val subreddits = remember(subredditsState) { subredditsState.getOrDefault(emptyList()) }
    val posts = remember(postsState) { postsState.getOrDefault(emptyList()) }
    val users = remember(usersState) { usersState.getOrDefault(emptyList()) }

    RainbowLazyVerticalGrid(appliedModifier, columns = GridCells.Fixed(columnsCount)) {

        item(span = { GridItemSpan(maxLineSpan) }) {
            ScrollableEnumTabRow(
                selectedTab,
                stateHolder::selectTab,
                icon = { Icon(it.icon, it.name) }
            )
        }

        when (selectedTab) {
            SearchTab.Subreddits -> {
                searchSubreddits(
                    subreddits,
                    onNavigateMainScreen,
                    onShowSnackbar,
                    stateHolder.subredditsStateHolder::setLastItem,
                )
            }

            SearchTab.Posts -> {
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
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.postsStateHolder::setLastItem,
                )
            }

            SearchTab.Users -> {
                users(
                    users,
                    onNavigateMainScreen,
                    onShowSnackbar,
                    stateHolder.usersStateHolder::setLastItem,
                )
            }
        }

        if (subredditsState.isLoading || usersState.isLoading || postsState.isLoading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                RainbowProgressIndicator()
            }
        }
    }

    DisposableEffect(selectedTab, selectedItemId) {
        if (selectedTab == SearchTab.Posts) {
            selectedItemId?.let { postId ->
                onNavigateDetailsScreen(DetailsScreen.Post(postId))
            }
        } else {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
        onDispose {}
    }
}

private val SearchTab.icon
    get() = when (this) {
        SearchTab.Subreddits -> RainbowIcons.GridView
        SearchTab.Posts -> RainbowIcons.Posts
        SearchTab.Users -> RainbowIcons.User
    }