package com.rainbow.app.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    onSearchClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var searchTerm by remember { mutableStateOf("") }
    val state by produceState<UIState<List<Subreddit>>>(UIState.Empty, searchTerm) {
        if (searchTerm.isNotBlank()) {
            Repos.Subreddit.searchSubreddit(searchTerm, SubredditsSearchSorting.Activity)
                .map { it.toUIState() }
                .collect { value = it }
            isExpanded = true
        }
    }

    Column(modifier) {
        OutlinedTextField(
            searchTerm,
            onValueChange = { searchTerm = it },
            placeholder = { Text(RainbowStrings.Search) },
            trailingIcon = {
                IconButton(onClick = { onSearchClick(searchTerm) }) {
                    Icon(RainbowIcons.Search, RainbowIcons.Search.name)
                }
            },
            singleLine = true,
            modifier = Modifier.onKeyEvent {
                if (it.key == Key.Enter)
                    onSearchClick(searchTerm)
                true
            }
        )
//        DropdownMenu(true, onDismissRequest = { isExpanded = false }) {
//            when (state) {
//                is UIState.Loading -> {
//                    RainbowProgressIndicator()
//                }
//                is UIState.Success -> {
//                    state.asSuccess().value.onEach {
//                        DropdownMenuItem(
//                            onClick = {
//                                onSubredditNameClick(it.name)
//                                isExpanded = false
//                            }
//                        ) {
//                            Text(it.name)
//                        }
//                    }
//                }
//                is UIState.Failure -> {
//                    Text("Failed loading subreddits.")
//                }
//                UIState.Empty -> {
//                    Text("Loading")
//                }
//            }
//        }
    }

}