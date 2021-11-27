package com.rainbow.app.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier
) {
    var cellCount by remember { mutableStateOf(3) }
    val scrollingState = rememberLazyListState()
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

    LazyColumn(
        modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged { size ->
                cellCount = when {
                    size.width < 1000 -> 2
                    size.width in 1000 until 1500 -> 3
                    else -> 4
                }
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = scrollingState,
    ) {
        val rows = if (subreddits.isEmpty()) 0 else 1 + (subreddits.size - 1) / cellCount
        item {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                modifier = Modifier.defaultPadding(),
                placeholder = { Text(RainbowStrings.FilterSubreddits) },
            )
        }
        items(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (columnIndex in 0 until cellCount) {
                    val itemIndex = rowIndex * cellCount + columnIndex
                    if (itemIndex < subreddits.size) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            when (subredditType) {
                                SubredditType.Default -> SubredditItem(subreddits[itemIndex], onClick, onShowSnackbar)
                                SubredditType.Search -> SearchSubredditItem(
                                    subreddits[itemIndex],
                                    onClick,
                                    onShowSnackbar
                                )
                            }
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}
