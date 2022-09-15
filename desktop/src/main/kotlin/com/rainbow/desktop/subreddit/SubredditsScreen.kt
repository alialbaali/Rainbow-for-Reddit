package com.rainbow.desktop.subreddit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.components.RainbowTextField
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.desktop.utils.*

@Composable
fun SubredditsScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { SubredditsScreenStateHolder.Instance }
    val subredditsState by stateHolder.subredditsStateHolder.items.collectAsState()
    val searchTerm by stateHolder.searchTerm.collectAsState()
    val subreddits = remember(subredditsState) { subredditsState.getOrDefault(emptyList()) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.medium),
        contentPadding = PaddingValues(MaterialTheme.dpDimensions.medium)
    ) {
        item(span = { GridItemSpan(4) }) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                RainbowTextField(
                    value = searchTerm,
                    onValueChange = stateHolder::setSearchTerm,
                    RainbowStrings.FilterSubreddits,
                )

                Text(RainbowStrings.SubredditsCount(subreddits.count()))
            }
        }

        itemsIndexed(subreddits) { index, subreddit ->
            SubredditItem(
                subreddit,
                onClick = { onNavigateMainScreen(MainScreen.Subreddit(subreddit.name)) },
                onShowSnackbar
            )
            PagingEffect(subreddits, index, stateHolder.subredditsStateHolder::setLastItem)
        }

        subredditsState
            .onLoading {
                item(span = { GridItemSpan(4) }) {
                    RainbowProgressIndicator()
                }
            }
            .onFailure {
                onShowSnackbar(it.message.toString())
            }
    }
}