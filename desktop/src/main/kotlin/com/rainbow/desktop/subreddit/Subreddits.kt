package com.rainbow.desktop.subreddit

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Subreddit

fun LazyGridScope.subreddits(
    state: UIState<List<Subreddit>>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Subreddit) -> Unit,
) {
    val subreddits = state.getOrDefault(emptyList())

    itemsIndexed(subreddits) { index, subreddit ->
        SubredditItem(
            subreddit,
            onClick = { onNavigateMainScreen(MainScreen.Subreddit(subreddit.name)) },
            onShowSnackbar
        )
        PagingEffect(index, subreddits.lastIndex) {
            onLoadMore(subreddit)
        }
    }

    if (state.isLoading) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            RainbowProgressIndicator()
        }
    }
}