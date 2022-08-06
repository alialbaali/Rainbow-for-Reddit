package com.rainbow.desktop.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.LazyGrid
import com.rainbow.common.components.RainbowTextField
import com.rainbow.common.model.ListModel
import com.rainbow.common.utils.*
import com.rainbow.domain.models.Subreddit

@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun CurrentUserSubredditsScreen(
    crossinline onClick: (String) -> Unit,
    noinline onSubredditUpdate: (Subreddit) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by CurrentUserSubredditsScreenModel.subredditListModel.items.collectAsState()
    val searchTerm by CurrentUserSubredditsScreenModel.searchTerm.collectAsState()
    OneTimeEffect(state.isLoading) {
        setListModel(CurrentUserSubredditsScreenModel.subredditListModel)
    }
    state
        .map { it.filter { it.isSubscribed }.filterContent(searchTerm) }
        .composed(onShowSnackbar, modifier) { subreddits ->
            LazyGrid(
                item = {
                    Row(
                        Modifier.fillParentMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        RainbowTextField(
                            value = searchTerm,
                            onValueChange = { CurrentUserSubredditsScreenModel.setSearchTerm(it) },
                            RainbowStrings.FilterSubreddits,
                        )

                        Text(RainbowStrings.SubredditsCount(subreddits.count()))
                    }
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