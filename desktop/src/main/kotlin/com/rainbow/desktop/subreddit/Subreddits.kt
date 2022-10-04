package com.rainbow.desktop.subreddit

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Subreddit

fun LazyGridScope.subreddits(
    favoriteSubreddits: List<Subreddit>,
    notFavoriteSubreddits: List<Subreddit>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    if (favoriteSubreddits.isNotEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                RainbowStrings.Favorites,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(favoriteSubreddits) { subreddit ->
            SubredditItem(
                subreddit,
                onClick = { onNavigateMainScreen(MainScreen.Subreddit(subreddit.name)) },
                onShowSnackbar
            )
        }
    }

    if (notFavoriteSubreddits.isNotEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                RainbowStrings.Subreddits,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(notFavoriteSubreddits) { subreddit ->
            SubredditItem(
                subreddit,
                onClick = { onNavigateMainScreen(MainScreen.Subreddit(subreddit.name)) },
                onShowSnackbar
            )
        }
    }
}

fun LazyGridScope.searchSubreddits(
    subreddits: List<Subreddit>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Subreddit) -> Unit,
) {
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
}