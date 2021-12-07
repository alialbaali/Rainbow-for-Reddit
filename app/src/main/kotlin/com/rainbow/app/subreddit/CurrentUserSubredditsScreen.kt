package com.rainbow.app.subreddit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.composed

@Composable
fun CurrentUserSubredditsScreen(
    onClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by CurrentUserSubredditsModel.subredditsModel.subreddits.collectAsState()
    state.composed(onShowSnackbar, modifier) { subreddits ->
        Subreddits(
            subreddits,
            SubredditType.Default,
            onClick = { onClick(it.name) },
            onLoadMore = { CurrentUserSubredditsModel.subredditsModel.setLastSubreddit(it) },
            onShowSnackbar,
            modifier
        )
    }
}