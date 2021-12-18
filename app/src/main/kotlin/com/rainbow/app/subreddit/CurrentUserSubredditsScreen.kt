package com.rainbow.app.subreddit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.model.ListModel
import com.rainbow.app.utils.composed

@Composable
fun CurrentUserSubredditsScreen(
    onClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    setListModel(CurrentUserSubredditsScreenModel.subredditListModel)
    val state by CurrentUserSubredditsScreenModel.subredditListModel.items.collectAsState()
    state.composed(onShowSnackbar, modifier) { subreddits ->
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