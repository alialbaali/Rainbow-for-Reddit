package com.rainbow.app.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.components.LazyGrid
import com.rainbow.app.components.RainbowTextField
import com.rainbow.app.model.ListModel
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.map
import com.rainbow.domain.models.Subreddit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentUserSubredditsScreen(
    onClick: (String) -> Unit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    setListModel(CurrentUserSubredditsScreenModel.subredditListModel)
    val state by CurrentUserSubredditsScreenModel.subredditListModel.items.collectAsState()
    val searchTerm by CurrentUserSubredditsScreenModel.searchTerm.collectAsState()
    state
        .map { it.filterContent(searchTerm) }
        .composed(onShowSnackbar, modifier) { subreddits ->
            LazyGrid(
                item = {
                    RainbowTextField(
                        value = searchTerm,
                        onValueChange = { CurrentUserSubredditsScreenModel.setSearchTerm(it) },
                        RainbowStrings.FilterSubreddits,
                    )
                }
            ) {
                items(subreddits) { subreddit ->
                    SubredditItem(
                        subreddit,
                        onSubredditUpdate,
                        onClick = { onClick(subreddit.name) },
                        onShowSnackbar
                    )
                }
            }
        }
}

private fun List<Subreddit>.filterContent(searchTerm: String) = filter {
    it.name.contains(searchTerm, ignoreCase = true) || it.shortDescription.contains(searchTerm)
}