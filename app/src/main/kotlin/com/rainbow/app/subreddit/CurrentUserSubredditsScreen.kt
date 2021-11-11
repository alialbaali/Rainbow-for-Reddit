package com.rainbow.app.subreddit

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
fun CurrentUserSubredditsScreen(
    onClick: (Subreddit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var lastSubreddit by remember { mutableStateOf<Subreddit?>(null) }
    val state by produceState<UIState<List<Subreddit>>>(UIState.Loading, lastSubreddit?.id) {
        Repos.Subreddit.getMySubreddits(lastSubreddit?.id)
            .map { it.toUIState() }
            .collect { value = it }
    }

    state.composed { subreddits ->
        Subreddits(
            subreddits,
            onClick = onClick,
            onLoadMore = { lastSubreddit = it }
        )
    }

}