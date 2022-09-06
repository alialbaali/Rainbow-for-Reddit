package com.rainbow.desktop.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.subreddit.subreddits
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.user.users

@Composable
inline fun SearchScreen(
    searchTerm: String,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(searchTerm) { SearchScreenStateHolder.getInstance(searchTerm) }
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val subredditsState by stateHolder.subredditsStateHolder.items.collectAsState()
    val postsState by stateHolder.postsStateHolder.items.collectAsState()
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

        item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(RainbowTheme.dpDimensions.medium)) }

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