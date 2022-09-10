package com.rainbow.desktop.search

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowLazyVerticalGrid
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.SortingItem
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.subreddit.subreddits
import com.rainbow.desktop.user.users

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
    val appliedModifier = remember(selectedTab) { if (selectedTab == SearchTab.Posts) modifier else Modifier }
    val columnsCount = remember(selectedTab) {
        when (selectedTab) {
            SearchTab.Subreddits -> 4
            SearchTab.Posts -> 1
            SearchTab.Users -> 4
        }
    }

    RainbowLazyVerticalGrid(appliedModifier, columns = GridCells.Fixed(columnsCount)) {

        item(span = { GridItemSpan(maxLineSpan) }) {
            ScrollableEnumTabRow(selectedTab, stateHolder::selectTab)
        }

        when (selectedTab) {
            SearchTab.Subreddits -> {
                subreddits(
                    subredditsState,
                    onNavigateMainScreen,
                    onShowSnackbar,
                    stateHolder.subredditsStateHolder::setLastItem,
                )
            }

            SearchTab.Posts -> {
                item {
                    SortingItem(
                        postsSorting,
                        postsTimeSorting,
                        stateHolder.postsStateHolder::setSorting,
                        stateHolder.postsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    postsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.postsStateHolder::setLastItem,
                )
            }

            SearchTab.Users -> {
                users(
                    usersState,
                    onNavigateMainScreen,
                    onShowSnackbar,
                    stateHolder.usersStateHolder::setLastItem,
                )
            }
        }
    }
}