package com.rainbow.desktop.search

import androidx.compose.foundation.layout.Column
//import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onSizeChanged
import com.rainbow.desktop.components.RainbowIconButton
import com.rainbow.desktop.components.RainbowTextField
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    onNavigateMainScreen: (MainScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val searchTerm by SearchTextFieldStateHolder.searchTerm.collectAsState()
//    val state by SearchTextFieldStateHolder.subredditListModel.items.collectAsState()
    var width by remember { mutableStateOf(0) }
    Column(modifier) {
        RainbowTextField(
            searchTerm,
            onValueChange = {
                isExpanded = it.isNotBlank()
                SearchTextFieldStateHolder.setSearchTerm(it)
            },
            RainbowStrings.Search,
            trailingIcon = {
                RainbowIconButton(
                    onClick = {
                        searchTerm.takeIf { it.isNotBlank() }?.let {
                            onNavigateMainScreen(MainScreen.Search(it))
                        }
                    },
                ) {
                    Icon(
                        RainbowIcons.Search,
                        RainbowIcons.Search.name,
                    )
                }
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
                        onNavigateMainScreen(MainScreen.Search(searchTerm))
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