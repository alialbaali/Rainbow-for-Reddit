package com.rainbow.common.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
//import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.rainbow.common.components.RainbowIconButton
import com.rainbow.common.components.RainbowTextField
import com.rainbow.common.subreddit.SearchSubredditMenuItem
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.composed
import com.rainbow.common.utils.defaultPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    onSearchClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val searchTerm by SearchTextFieldModel.searchTerm.collectAsState()
    val state by SearchTextFieldModel.subredditListModel.items.collectAsState()
    var width by remember { mutableStateOf(0) }
    Column(modifier) {
        RainbowTextField(
            searchTerm,
            onValueChange = {
                isExpanded = it.isNotBlank()
                SearchTextFieldModel.setSearchTerm(it)
            },
            RainbowStrings.Search,
            trailingIcon = {
                RainbowIconButton(
                    RainbowIcons.Search,
                    RainbowIcons.Search.name,
                    onClick = { searchTerm.takeIf { it.isNotBlank() }?.let(onSearchClick) }
                )
            },
            modifier = Modifier
                .onSizeChanged { width = it.width }
                .onFocusEvent {
                    if (it.isFocused && searchTerm.isNotBlank())
                        isExpanded = !isExpanded
                }
                .onKeyEvent {
                    if (it.key == Key.Enter && searchTerm.isNotBlank()) {
                        isExpanded = false
                        onSearchClick(searchTerm)
                    }
                    false
                },
        )
//        DropdownMenu(
//            isExpanded,
//            onDismissRequest = { isExpanded = false },
//            focusable = false,
//            Modifier.width(width.dp)
//        ) {
//            state.composed(
//                onShowSnackbar = null,
//                modifier = Modifier.defaultPadding(),
//                onFailure = { Text("Failed loading subreddits.") }
//            ) {
//                it.takeIf { it.isNotEmpty() }
//                    ?.take(10)
//                    ?.onEach { subreddit ->
//                        SearchSubredditMenuItem(
//                            subreddit,
//                            onSubredditClick = {
//                                isExpanded = false
//                                onSubredditNameClick(subreddit.name)
//                            }
//                        )
//                    } ?: Text("No matching subreddits.", Modifier.defaultPadding(16.dp))
//            }
//        }
    }
}