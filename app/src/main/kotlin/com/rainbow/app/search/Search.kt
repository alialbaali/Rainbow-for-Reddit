package com.rainbow.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.components.TextImage
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
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
            isExpanded = true
            value = UIState.Loading
//            Repos.Subreddit.searchSubreddit(searchTerm, SubredditsSearchSorting.Activity)
        } else {
            isExpanded = false
        }
    }
    var width by remember { mutableStateOf(0) }

    Column(modifier) {
        OutlinedTextField(
            searchTerm,
            onValueChange = { searchTerm = it },
            placeholder = { Text(RainbowStrings.Search) },
            trailingIcon = {
                IconButton(onClick = { searchTerm.takeIf { it.isNotBlank() }?.let(onSearchClick) }) {
                    Icon(RainbowIcons.Search, RainbowIcons.Search.name)
                }
            },
            singleLine = true,
            modifier = modifier
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
                            DropdownMenuItem(
                                onClick = {
                                    onSubredditNameClick(subreddit.name)
                                    isExpanded = false
                                }
                            ) {
                                val painterResource =
                                    lazyPainterResource(subreddit.imageUrl ?: subreddit.bannerImageUrl.toString())
                                val ImageModifier = Modifier.size(24.dp).clip(MaterialTheme.shapes.small)
                                KamelImage(
                                    painterResource,
                                    subreddit.name,
                                    ImageModifier,
                                    onLoading = { RainbowProgressIndicator() },
                                    onFailure = {
                                        TextImage(
                                            subreddit.name,
                                            ImageModifier.background(MaterialTheme.colors.secondary),
                                            fontSize = 16.sp
                                        )
                                    }
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(subreddit.name)
                            }
                        } ?: Text("No matching subreddits.", Modifier.padding(16.dp))
                }
                is UIState.Failure -> Text("Failed loading subreddits.")
                UIState.Empty -> Text("Loading")
            }
        }
    }

}