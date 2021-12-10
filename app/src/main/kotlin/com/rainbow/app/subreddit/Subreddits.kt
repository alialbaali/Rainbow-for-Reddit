package com.rainbow.app.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.components.LazyGrid
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.Subreddit

enum class SubredditType { Default, Search, }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Subreddits(
    subreddits: List<Subreddit>,
    subredditType: SubredditType,
    onClick: (Subreddit) -> Unit,
    onLoadMore: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchTerm by remember { mutableStateOf("") }
    val subreddits = subreddits.let {
        searchTerm.filter { !it.isWhitespace() }
            .takeIf { it.isNotBlank() }
            ?.let { searchTerm ->
                subreddits.filter {
                    it.name.contains(searchTerm, ignoreCase = true) || it.shortDescription.contains(searchTerm)
                }
            } ?: subreddits
    }

    LazyGrid(
        item = {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                modifier = Modifier.defaultPadding(),
                placeholder = { Text(RainbowStrings.FilterSubreddits) },
            )
        }
    ) {
        items(subreddits) { subreddit ->
            when (subredditType) {
                SubredditType.Default -> SubredditItem(subreddit, onClick, onShowSnackbar)
                SubredditType.Search -> SearchSubredditItem(subreddit, onClick, onShowSnackbar)
            }
        }
    }
}
