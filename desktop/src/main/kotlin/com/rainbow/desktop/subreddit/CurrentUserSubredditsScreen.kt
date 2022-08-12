package com.rainbow.desktop.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.LazyGrid
import com.rainbow.desktop.components.RainbowTextField
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.composed
import com.rainbow.desktop.utils.filterContent
import com.rainbow.desktop.utils.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun CurrentUserSubredditsScreen(
    crossinline onNavigate: (Screen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val state by CurrentUserSubredditsScreenStateHolder.subredditListModel.items.collectAsState()
    val searchTerm by CurrentUserSubredditsScreenStateHolder.searchTerm.collectAsState()
//    state
//        .map { it.filter { it.isSubscribed }.filterContent(searchTerm) }
//        .composed(onShowSnackbar, modifier) { subreddits ->
//            LazyGrid(
//                item = {
//                    Row(
//                        Modifier.fillParentMaxWidth(),
//                        Arrangement.SpaceBetween,
//                        Alignment.CenterVertically
//                    ) {
//                        RainbowTextField(
//                            value = searchTerm,
//                            onValueChange = { CurrentUserSubredditsScreenStateHolder.setSearchTerm(it) },
//                            RainbowStrings.FilterSubreddits,
//                        )
//
//                        Text(RainbowStrings.SubredditsCount(subreddits.count()))
//                    }
//                }
//            ) {
//                items(subreddits) { subreddit ->
//                    SubredditItem(
//                        subreddit,
//                        {},
//                        onClick = { onNavigate(Screen.Subreddit(subreddit.name)) },
//                        onShowSnackbar
//                    )
//                }
//            }
//        }
}