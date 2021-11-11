package com.rainbow.app.subreddit

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.PagingEffect
import com.rainbow.domain.models.Subreddit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Subreddits(
    subreddits: List<Subreddit>,
    onClick: (Subreddit) -> Unit,
    onLoadMore: (Subreddit) -> Unit,
    modifier: Modifier = Modifier
) {
    var cellCount by remember { mutableStateOf(3) }
    LazyVerticalGrid(
        GridCells.Fixed(animateIntAsState(cellCount).value),
        modifier.onSizeChanged { size ->
            cellCount = when {
                size.width < 1000 -> 2
                size.width in 1000 until 1500 -> 3
                else -> 4
            }
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(subreddits) { index, subreddit ->
            SubredditItem(subreddit, onClick)
            PagingEffect(subreddits, index, onLoadMore)
        }
    }
}
