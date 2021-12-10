package com.rainbow.app.subreddit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.map

@Composable
fun CurrentUserSubredditsScreen(
    onClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by CurrentUserSubredditsScreenModel.subredditListModel.items.collectAsState()
    state.map { it.sortedBy { subreddit -> subreddit.name } }.composed(onShowSnackbar, modifier) { subreddits ->
        Subreddits(
            subreddits,
            SubredditType.Default,
            onClick = { onClick(it.name) },
            onLoadMore = { CurrentUserSubredditsScreenModel.subredditListModel.setLastItem(it) },
            onShowSnackbar,
            modifier
        )
    }
}