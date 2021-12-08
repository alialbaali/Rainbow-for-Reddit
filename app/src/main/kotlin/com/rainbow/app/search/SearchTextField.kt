package com.rainbow.app.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.subreddit.SearchSubredditMenuItem
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.asSuccess


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    onSearchClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchTerm by SearchModel.searchTerm.collectAsState()
    var isExpanded by remember(searchTerm) { mutableStateOf(searchTerm.isNotBlank()) }
    val state by SearchModel.subredditsModel.subreddits.collectAsState()
    var width by remember { mutableStateOf(0) }
    Column(modifier) {
        OutlinedTextField(
            searchTerm,
            onValueChange = { SearchModel.setSearchTerm(it) },
            placeholder = { Text(RainbowStrings.Search) },
            trailingIcon = {
                IconButton(onClick = { searchTerm.takeIf { it.isNotBlank() }?.let(onSearchClick) }) {
                    Icon(RainbowIcons.Search, RainbowIcons.Search.name)
                }
            },
            singleLine = true,
            modifier = Modifier
                .onSizeChanged { width = it.width }
                .onKeyEvent {
                    if (it.key == Key.Enter)
                        searchTerm.takeIf { it.isNotBlank() }?.let(onSearchClick)
                    false
                }
        )
        DropdownMenu(
            isExpanded,
            onDismissRequest = { isExpanded = false },
            focusable = false,
            Modifier.width(width.dp)
        ) {
            when (state) {
                is UIState.Loading -> RainbowProgressIndicator()
                is UIState.Success -> {
                    state.asSuccess().value
                        .takeIf { it.isNotEmpty() }
                        ?.take(10)
                        ?.onEach { subreddit ->
                            SearchSubredditMenuItem(
                                subreddit, onSubredditClick = {
                                    onSubredditNameClick(subreddit.name)
                                    isExpanded = false
                                }
                            )
                        } ?: Text("No matching subreddits.", Modifier.padding(16.dp))
                }
                is UIState.Failure -> Text("Failed loading subreddits.")
                UIState.Empty -> Text("Loading")
            }
        }
    }
}