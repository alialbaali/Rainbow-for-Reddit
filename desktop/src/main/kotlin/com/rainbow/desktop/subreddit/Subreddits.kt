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

inline fun LazyGridScope.subreddits(
    state: UIState<List<Subreddit>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onLoadMore: (Subreddit) -> Unit,
) {
    val subreddits = state.getOrDefault(emptyList())

    itemsIndexed(subreddits) { index, subreddit ->
        SubredditItem(
            subreddit,
            onClick = { onNavigateMainScreen(MainScreen.Subreddit(subreddit.name)) },
            onShowSnackbar
        )
        PagingEffect(subreddits, index, onLoadMore)
    }

    if (state.isLoading) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            RainbowProgressIndicator()
        }
    }
}